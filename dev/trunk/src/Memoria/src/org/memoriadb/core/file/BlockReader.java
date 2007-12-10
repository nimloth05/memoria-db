package org.memoriadb.core.file;

import java.io.*;

import org.memoriadb.block.Block;
import org.memoriadb.core.block.IBlockErrorHandler;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.load.HydratedObject;
import org.memoriadb.core.util.MemoriaCRC32;
import org.memoriadb.id.*;

/**
 * Reads in a block, assuming that the given stream is consistent with the given position in the file.
 * 
 * The HeadRevision is the highest
 * 
 * @author msc
 */
public class BlockReader {

  private long fRevision = 0;

  public BlockReader() {}

  public long getRevision() {
    return fRevision;
  }

  /**
   * @param lastWrittenBlockInfo
   * @return Number of read bytes in this function
   * @throws IOException
   */
  public long readBlock(DataInputStream stream, Block block, IObjectIdFactory idFactory, IFileReaderHandler handler,
      IBlockErrorHandler errorHandler) throws IOException {
    if (!FileLayout.testBlockTag(stream))  return errorHandler.blockTagCorrupt(stream, block); 

    MemoriaCRC32 crc = new MemoriaCRC32();
    
    // block size
    long blockSize = stream.readLong(); 
    block.setBodySize(blockSize);
    crc.updateLong(blockSize);

    long readCrc = stream.readLong();
    if (readCrc != crc.getValue()) return errorHandler.blockSizeCorrupt(stream, block); 

    byte[] transactionData = readTransaction(stream, block, errorHandler);
    if(transactionData == null) return blockSize + FileLayout.BLOCK_OVERHEAD;

    // no state was changed before this line!
    handler.block(block);

    int objectCount = readObjects(idFactory, handler, fRevision, transactionData);
    block.setNumberOfObjectData(objectCount);

    return blockSize + FileLayout.BLOCK_OVERHEAD;
  }

  private void readObject(IObjectIdFactory idFactory, IFileReaderHandler handler, long revision, byte[] data, int offset, int size)
      throws IOException {
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data, offset, size));

    IObjectId typeId = idFactory.createFrom(stream);
    IObjectId objectId = idFactory.createFrom(stream);

    if (idFactory.isObjectDeletionMarker(typeId)) {
      handler.objectDeleted(objectId, revision);
      return;
    }
    else if (idFactory.isMemoriaClassDeletionMarker(typeId)) {
      handler.memoriaClassDeleted(objectId, revision);
      return;
    }

    // no deleteMarker encountered
    if (idFactory.isMemoriaFieldClass(typeId) || idFactory.isMemoriaHandlerClass(typeId)) {
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

  private byte[] readTransaction(DataInputStream stream, Block block, IBlockErrorHandler errorHandler) throws IOException {
    MemoriaCRC32 crc;
    crc = new MemoriaCRC32();
    long transactionSize = stream.readLong(); // transactionsize
    crc.updateLong(transactionSize);

    fRevision = stream.readLong(); // transaction-revision
    crc.updateLong(fRevision);
    byte[] transactionData = new byte[(int) transactionSize];
    stream.readFully(transactionData);
    crc.update(transactionData);

    long expectedCrc32 = stream.readLong();
    long value = crc.getValue();
    if (value != expectedCrc32){
      errorHandler.transactionCorrupt(stream, block);
      return null;
    }

    // block may be bigger then the transaction-data -> skip
    skip(stream, block.getBodySize() - transactionSize - FileLayout.TRX_OVERHEAD);
    
    return transactionData;
  }

  private void skip(DataInputStream stream, long size) throws IOException {
    long skip = stream.skip(size);
    if (skip != size) { throw new MemoriaException("could not skip bytes: " + size); }
  }

}
