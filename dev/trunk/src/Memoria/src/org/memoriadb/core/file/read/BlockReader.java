/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
 * @author Sandro
 */
public class BlockReader {

  private static final long REVISION_FOR_ERROR_CONDITION = 0;

  private final ICompressor fCompressor;

  private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

  public BlockReader(ICompressor compressor) {
    fCompressor = compressor;
  }
  
  /**
   * @return Number of read bytes in this function
   * @throws IOException
   */
  public long readBlock(MemoriaDataInputStream stream, Block block, IObjectIdFactory idFactory, IFileReaderHandler handler, IBlockErrorHandler errorHandler) throws IOException {
    if (!FileLayout.testBlockTag(stream)) {
      errorHandler.blockTagCorrupt(stream, block);
      return REVISION_FOR_ERROR_CONDITION;
    }

    // block size
    long blockSize = stream.readLong();

    // blockSize + size-crc + trx-crc
    if (blockSize + FileLayout.CRC_LEN + FileLayout.CRC_LEN > stream.available()) {
      errorHandler.transactionCorrupt(stream, block);
      return REVISION_FOR_ERROR_CONDITION;
    }

    long readCrc = stream.readLong();

    // long blockSize = stream.readUnsignedLong();
    MemoriaCRC32 crc = new MemoriaCRC32();
    crc.updateLong(blockSize);
    if (readCrc != crc.getValue()) {
      errorHandler.blockSizeCorrupt(stream, block);
      return REVISION_FOR_ERROR_CONDITION;
    }

    block.setBodySize(blockSize);

    byte[] body = new byte[(int) blockSize];
    stream.readFully(body);
    readCrc = stream.readLong();

    crc = new MemoriaCRC32();
    crc.update(body);
    if (readCrc != crc.getValue()) {
      errorHandler.transactionCorrupt(stream, block);
      return REVISION_FOR_ERROR_CONDITION;
    }

    // now expand the transaction
    body = fCompressor.decompress(body);

    DataInputStream dis = new DataInputStream(new ByteArrayInputStream(body));
    long revision = dis.readLong(); // transaction-revision
    block.setRevision(revision);

    long objectDataCount = dis.readLong();

    // no state was changed before this line!
    handler.block(block);

    readObjects(block, idFactory, handler, body, FileLayout.TRX_OVERHEAD, objectDataCount);

    return revision;
  }

  private byte[] copyDataRange(byte[] data, int from, int to) {
    if (from == to) {
      return EMPTY_BYTE_ARRAY;
    }
    return Arrays.copyOfRange(data, from, to);
  }

  private void readObject(Block block, IObjectIdFactory idFactory, IFileReaderHandler handler, byte[] data, int offset, int size) throws IOException {
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data, offset, size));

    IObjectId typeId = idFactory.createFrom(stream);
    IObjectId objectId = idFactory.createFrom(stream);
    
    block.addObjectId(objectId);

    if (idFactory.isObjectDeletionMarker(typeId)) {
      handler.objectDeleted(objectId);
      return;
    }
    else if (idFactory.isMemoriaClassDeletionMarker(typeId)) {
      handler.memoriaClassDeleted(objectId);
      return;
    }

    // no deleteMarker encountered
    byte[] objectData = copyDataRange(data, offset + 2 * idFactory.getIdSize(), offset + size);
    if (idFactory.isMemoriaFieldClass(typeId) || idFactory.isMemoriaHandlerClass(typeId)) {
      handler.memoriaClass(new HydratedObject(typeId, objectData), objectId, size + FileLayout.OBJECT_SIZE_LEN);
    }
    else {
      handler.object(new HydratedObject(typeId, objectData), objectId, size + FileLayout.OBJECT_SIZE_LEN);
    }
  }

  /**
   * @param block 
   * @param objectDataCount
   * @return The number of read ObjectData
   */
  private int readObjects(Block block, IObjectIdFactory idFactory, IFileReaderHandler handler, byte[] data, int offset, long objectDataCount) throws IOException {
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data, offset, data.length - offset));
    for (int i = 0; i < objectDataCount; ++i) {
      int size = stream.readInt();
      byte[] objectData = new byte[size];
      stream.readFully(objectData);

      readObject(block, idFactory, handler, objectData, 0, size);
    }
    return 0;
  }

}
