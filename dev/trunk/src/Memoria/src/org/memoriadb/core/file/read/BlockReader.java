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

import java.io.IOException;
import java.nio.ByteBuffer;

import org.memoriadb.block.Block;
import org.memoriadb.core.block.IBlockErrorHandler;
import org.memoriadb.core.file.*;
import org.memoriadb.core.util.MemoriaCRC32;
import org.memoriadb.core.util.io.*;
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

  public BlockReader(ICompressor compressor) {
    fCompressor = compressor;
  }
  
  /**
   * @return Number of read bytes in this function
   * @throws IOException
   */
  public long readBlock(IDataInput stream, Block block, IObjectIdFactory idFactory, IFileReaderHandler handler, IBlockErrorHandler errorHandler) throws IOException {
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

    IDataInput dis = new ByteBufferDataInput(body);
    long revision = dis.readLong(); // transaction-revision
    block.setRevision(revision);

    long objectDataCount = dis.readLong();

    // no state was changed before this line!
    handler.block(block);

    readObjects(block, idFactory, handler, dis, objectDataCount);

    return revision;
  }

  private void readObject(Block block, IObjectIdFactory idFactory, IFileReaderHandler handler, IDataInput stream, int size) throws IOException {
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

    ByteBuffer objectData = stream.readBytes(stream.available());
    HydratedObject hydratedObject = new HydratedObject(typeId, objectData);
    
    if (idFactory.isMemoriaFieldClass(typeId) || idFactory.isMemoriaHandlerClass(typeId)) {
      handler.memoriaClass(hydratedObject, objectId, size + FileLayout.OBJECT_SIZE_LEN);
    }
    else {
      handler.object(hydratedObject, objectId, size + FileLayout.OBJECT_SIZE_LEN);
    }
  }

  /**
   * @param block 
   * @param objectDataCount
   * @return The number of read ObjectData
   */
  private int readObjects(Block block, IObjectIdFactory idFactory, IFileReaderHandler handler, IDataInput input, long objectDataCount) throws IOException {
    for (int i = 0; i < objectDataCount; ++i) {
      int size = input.readInt();
      ByteBufferDataInput subInput = new ByteBufferDataInput(input.readBytes(size));
      readObject(block, idFactory, handler, subInput, size);
    }
    return 0;
  }

}
