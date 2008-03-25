package org.memoriadb.core.file.read;

import java.io.*;
import java.util.Arrays;

import org.memoriadb.block.Block;
import org.memoriadb.core.block.IBlockErrorHandler;
import org.memoriadb.core.file.*;
import org.memoriadb.core.util.MemoriaCRC32;
import org.memoriadb.core.util.io.MemoriaDataInputStream;
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
  private final ICompressor fCompressor;

  public BlockReader(ICompressor compressor) {
    fCompressor = compressor;
  }

  public long getRevision() {
    return fRevision;
  }

  /**
   * @param lastWrittenBlockInfo
   * @return Number of read bytes in this function
   * @throws IOException
   */
  public void readBlock(MemoriaDataInputStream stream, Block block, IObjectIdFactory idFactory, IFileReaderHandler handler, IBlockErrorHandler errorHandler) throws IOException {
    if (!FileLayout.testBlockTag(stream)) {
      errorHandler.blockTagCorrupt(stream, block);
      return;
    }

    // block size
    long blockSize = stream.readLong();

    // blockSize + size-crc + trx-crc
    if (blockSize + FileLayout.CRC_LEN + FileLayout.CRC_LEN > stream.available()) {
      errorHandler.transactionCorrupt(stream, block);
      return;
    }

    long readCrc = stream.readLong();

    // long blockSize = stream.readUnsignedLong();
    MemoriaCRC32 crc = new MemoriaCRC32();
    crc.updateLong(blockSize);
    if (readCrc != crc.getValue()) {
      errorHandler.blockSizeCorrupt(stream, block);
      return;
    }

    block.setBodySize(blockSize);

    byte[] body = new byte[(int) blockSize];
    stream.readFully(body);
    readCrc = stream.readLong();

    crc = new MemoriaCRC32();
    crc.update(body);
    if (readCrc != crc.getValue()) {
      errorHandler.transactionCorrupt(stream, block);
      return;
    }

    // now expand the transaction
    body = fCompressor.decompress(body);

    DataInputStream dis = new DataInputStream(new ByteArrayInputStream(body));
    fRevision = dis.readLong(); // transaction-revision

    long objectDataCount = dis.readLong();
    block.setObjectDataCount(objectDataCount);

    // no state was changed before this line!
    handler.block(block);

    readObjects(idFactory, handler, fRevision, body, FileLayout.TRX_OVERHEAD, objectDataCount);

    return;
  }

  private void readObject(IObjectIdFactory idFactory, IFileReaderHandler handler, long revision, byte[] data, int offset, int size) throws IOException {
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data, offset, size));

    IObjectId typeId = idFactory.createFrom(stream);
    IObjectId objectId = idFactory.createFrom(stream);

    byte[] objectData = Arrays.copyOfRange(data, offset + 2 * idFactory.getIdSize(), offset + size);

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
      handler.memoriaClass(new HydratedObject(typeId, objectData), objectId, revision, size + FileLayout.OBJECT_SIZE_LEN);
    }
    else {
      handler.object(new HydratedObject(typeId, objectData), objectId, revision, size + FileLayout.OBJECT_SIZE_LEN);
    }
  }

  /**
   * @param objectDataCount
   * @return The number of read ObjectData
   */
  private int readObjects(IObjectIdFactory idFactory, IFileReaderHandler handler, long revision, byte[] data, int offset, long objectDataCount) throws IOException {
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data, offset, data.length - offset));
    for (int i = 0; i < objectDataCount; ++i) {
      int size = stream.readInt();
      byte[] objectData = new byte[size];
      stream.readFully(objectData);

      readObject(idFactory, handler, revision, objectData, 0, size);
    }
    return 0;
  }

}
