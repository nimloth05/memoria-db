package org.memoriadb.core.file;

import java.io.*;

import org.memoriadb.core.block.Block;
import org.memoriadb.core.id.*;
import org.memoriadb.core.load.HydratedObject;
import org.memoriadb.exception.*;
import org.memoriadb.util.MemoriaCRC32;

/**
 * Reads the content of a {@link IMemoriaFile}.
 * 
 * This protocol must be strictly adhered to
 * 
 * 1. Create the FileReader 
 * 2. readHeader() - file is opened
 * 3. readBlock
 * 4. close()
 * 
 * @author msc
 */
public class FileReader {
  
  private enum State{created, headerRead, blockRead, closed};

  
  private final IMemoriaFile fFile;
  private State state = State.created;
  private DataInputStream fStream;
  
  // the current position in the file
  private long fPosition = 0;
  private long fHeadRevision;

  public FileReader(IMemoriaFile file) {
    fFile = file;
  }

  public void close() {
    if(state == State.created) return;
    
    checkState(State.blockRead, State.closed);
    
    if(fStream == null) return;
    
    try {
      fStream.close();
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }

  /**
   * Reads all blocks and closes the file
   */
  public long readBlocks(IObjectIdFactory idFactory, IFileReaderHandler handler)  throws IOException {
    checkState(State.headerRead, State.blockRead);
    
    // read file header
    while (fStream.available() > 0) {
      fPosition += readBlock(idFactory, handler, fStream, fPosition);
    }
    
    fStream.close();
    
    return fHeadRevision;
  }

  public FileHeader readHeader() throws IOException {
    checkState(State.created, State.headerRead);
    
    fStream = new DataInputStream(fFile.getInputStream());
    
    FileHeader result = FileHeaderHelper.readHeader(fStream);
    fPosition = result.getHeaderSize();
    
    return result;
  }

  private void checkState(State expectedState, State nextState) {
    if(state != expectedState) throw new MemoriaException("wrong state. Expected " + expectedState +" but was " + state);
    state = nextState;
  }

  /**
   * @return Number of read bytes in this function
   * @throws IOException
   */
  private long readBlock(IObjectIdFactory idFactory, IFileReaderHandler handler, DataInputStream stream, long position) throws IOException {
    FileLayout.assertBlockTag(stream);
    
    MemoriaCRC32 crc32 = new MemoriaCRC32();
    long blockSize = stream.readLong(); // the block size
    crc32.updateLong(blockSize);
    
    long transactionSize = stream.readLong(); // transactionsize
    crc32.updateLong(transactionSize);
    
    long revision = stream.readLong(); // transaction-revision
    crc32.updateLong(revision);
    fHeadRevision = Math.max(fHeadRevision, revision);

    byte[] transactionData = new byte[(int) transactionSize];
    stream.read(transactionData);
    crc32.update(transactionData);

    long expectedCrc32 = stream.readLong();
    long value = crc32.getValue();
    if (value != expectedCrc32) throw new FileCorruptException("wrong checksum for block at position " + position);

    // block may be bigger then the transaction-data -> skip
    skip(stream, blockSize - transactionSize - (8 + 8 + 8)); // (transactionSize + crc32)


    
    Block block = new Block(blockSize, position);
    // first handle block, later handle objects. The number of the ObjectData in the block is not yet resolved!
    handler.block(block);
    
    int objectCount = readObjects(idFactory, handler, revision,  transactionData);
    block.setNumberOfObjectData(objectCount);
    
    // revision + startTag + blockSize dataSize + data.length
    return blockSize + FileLayout.BLOCK_OVERHEAD;
  }

  private void readObject(IObjectIdFactory idFactory, IFileReaderHandler handler, long revision, byte[] data, int offset, int size) throws IOException {
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data, offset, size));

    IObjectId typeId = idFactory.createFrom(stream);
    IObjectId objectId = idFactory.createFrom(stream);
    
    if(idFactory.isObjectDeletionMarker(typeId)) {
      handler.objectDeleted(objectId, revision);
      return;
    }
    else if (idFactory.isMemoriaClassDeletionMarker(typeId)) {
      handler.memoriaClassDeleted(objectId, revision);
      return;
    }

    // no deleteMarker encountered
    if (idFactory.isMemoriaClass(typeId)) {
      handler.memoriaClass(new HydratedObject(typeId, stream), objectId, revision, size + FileLayout.OBJECT_SIZE_LEN);
    }
    else {
      handler.object(new HydratedObject(typeId, stream), objectId, revision, size + FileLayout.OBJECT_SIZE_LEN);
    }
  }

  /**
   * @return The number of read ObjectData
   */
  private int readObjects(IObjectIdFactory idFactory, IFileReaderHandler handler, long revision, byte[] data) throws IOException {
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data));
    int offset = 0;
    int objectCount = 0;
    while (stream.available() > 0) {
      ++objectCount;
      int size = stream.readInt();
      readObject(idFactory, handler, revision, data, offset + FileLayout.OBJECT_SIZE_LEN, size);
      offset += FileLayout.OBJECT_SIZE_LEN + size;
      skip(stream, size);
    }
    return objectCount;
  }
  
  private void skip(DataInputStream stream, long size) throws IOException {
    if (stream.skip(size) != size) throw new RuntimeException("could not skip bytes: " + size);
  }

}
