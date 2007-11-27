package org.memoriadb.core.block;

import java.io.*;

import org.memoriadb.block.Block;
import org.memoriadb.core.file.*;
import org.memoriadb.core.id.*;
import org.memoriadb.core.load.HydratedObject;
import org.memoriadb.exception.FileCorruptException;
import org.memoriadb.util.MemoriaCRC32;

/**
 * Reads in a block, assuming that the given stream is consistent with the given position in the file.
 * 
 * The HeadRevision is the highest   
 *  
 * @author msc
 */
public class BlockReader {
  
  private long fRevision = 0;

  public BlockReader() {
  }

  public long getRevision() {
    return fRevision;
  }

  /**
   * @return Number of read bytes in this function
   * @throws IOException
   */
  public long readBlock(DataInputStream stream, Block block, IObjectIdFactory idFactory, IFileReaderHandler handler) throws IOException {
    FileLayout.assertBlockTag(stream);
    
    MemoriaCRC32 crc32 = new MemoriaCRC32();
    long blockSize = stream.readLong(); // the block size
    crc32.updateLong(blockSize);
    
    long transactionSize = stream.readLong(); // transactionsize
    crc32.updateLong(transactionSize);
    
    fRevision = stream.readLong(); // transaction-revision
    crc32.updateLong(fRevision);
    byte[] transactionData = new byte[(int) transactionSize];
    stream.read(transactionData);
    crc32.update(transactionData);

    long expectedCrc32 = stream.readLong();
    long value = crc32.getValue();
    if (value != expectedCrc32) throw new FileCorruptException("wrong checksum");

    // block may be bigger then the transaction-data -> skip
    skip(stream, blockSize - transactionSize - (8 + 8 + 8)); // (transactionSize + crc32)
    
    block.setSize(blockSize);
    handler.block(block);
    
    int objectCount = readObjects(idFactory, handler, fRevision,  transactionData);
    block.setNumberOfObjectData(objectCount);
    
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
    if (idFactory.isMemoriaFieldClass(typeId)  || idFactory.isMemoriaHandlerClass(typeId)) {
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
