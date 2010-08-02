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
import org.memoriadb.core.file.read.IFileReaderHandler;

import java.io.DataInputStream;

public class LastWrittenErrorHandler extends AbstractBlockErrorHandler {

  public LastWrittenErrorHandler(IFileReaderHandler fileReaderHandler) {
    super(fileReaderHandler);
  }
  
  @Override
  public void transactionCorrupt(DataInputStream input, Block block) {
   //BlockReader.skip(input, block.getBodySize() - transactionSize - FileLayout.TRX_OVERHEAD);
  }

}
