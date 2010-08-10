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

import java.io.IOException;

import org.memoriadb.block.Block;
import org.memoriadb.core.file.IMemoriaFile;
import org.memoriadb.core.file.read.IFileReaderHandler;
import org.memoriadb.core.util.io.IDataInput;

/**
 * 
 * Used when the currently processed block is the last written block
 * 
 * @author msc
 * 
 */
public class LastAppendedErrorHandler extends AbstractBlockErrorHandler {

  private final IMemoriaFile fFile;

  public LastAppendedErrorHandler(IFileReaderHandler fileReaderHandler, IMemoriaFile file) {
    super(fileReaderHandler);
    fFile = file;
  }

  @Override
  public void blockSizeCorrupt(IDataInput input, Block block) throws IOException {
    freeCorruptBlock(input, block);
    skipToEnd(input);
  }

  @Override
  public void blockTagCorrupt(IDataInput input, Block block) throws IOException {
    freeCorruptBlock(input, block);
    skipToEnd(input);
  }

  @Override
  public void transactionCorrupt(IDataInput input, Block block) throws IOException {
    freeCorruptBlock(input, block);
  }

  private void freeCorruptBlock(IDataInput input, Block block) throws IOException {
    long size = fFile.getSize() - block.getPosition();
    block.setWholeSize(size);
    block.setIsFree();
    fFileReaderHandler.block(block);

    skipToEnd(input);
    
  }

  private void skipToEnd(IDataInput input) throws IOException {
    // skip stream to the end
    input.skipBytes(Integer.MAX_VALUE);
  }

}
