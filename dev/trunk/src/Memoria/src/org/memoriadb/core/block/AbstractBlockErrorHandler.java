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

package org.memoriadb.core.block;

import org.memoriadb.block.Block;
import org.memoriadb.core.exception.FileCorruptException;
import org.memoriadb.core.file.read.IFileReaderHandler;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Default-behaviour for when a part of a block is corrupt: throw
 * 
 * @author msc
 *
 */
public abstract class AbstractBlockErrorHandler implements IBlockErrorHandler {

  protected final IFileReaderHandler fFileReaderHandler;
  
  public AbstractBlockErrorHandler(IFileReaderHandler fileReaderHandler) {
    fFileReaderHandler = fileReaderHandler;
  }

  @SuppressWarnings("unused")
  @Override
  public void blockSizeCorrupt(DataInputStream input, Block block) throws IOException {
    throw new FileCorruptException("block size corrupt: " + block);
  }

  @SuppressWarnings("unused")
  @Override
  public void blockTagCorrupt(DataInputStream input, Block block) throws IOException {
    throw new FileCorruptException("block tag corrupt: " + block);
  }

  @SuppressWarnings("unused")
  @Override
  public void transactionCorrupt(DataInputStream input, Block block) throws IOException {
    throw new FileCorruptException("transaction corrupt in block: " + block);
  }

}
