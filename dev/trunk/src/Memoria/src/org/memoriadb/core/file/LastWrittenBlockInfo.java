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

package org.memoriadb.core.file;

import org.memoriadb.block.Block;

/**
 * 
 * Information about the last block that was written.
 * 
 * @author msc
 *
 */
public class LastWrittenBlockInfo {
  private final int fWriteMode;
  private final long fPosition;
  
  public static LastWrittenBlockInfo createNoLastWrittenBlock() {
    return new LastWrittenBlockInfo(-1,-1);
  }

  public LastWrittenBlockInfo(int writeMode, long position) {
    fWriteMode = writeMode;
    fPosition = position;
  }
  
  public long getPosition() {
    return fPosition;
  }

  /**
   * @return false, if the file has no blocks
   */
  public boolean hasLastWrittenBlock() {
    return fPosition != HeaderHelper.NO_CURRENT_BLOCK;
  }

  /**
   * @return true, if the last written block was appended to the file.
   */
  public boolean isAppend() {
    return fWriteMode == FileLayout.WRITE_MODE_APPEND;
  }

  public boolean isLastWritten(Block block) {
    return fPosition == block.getPosition();
  }
  
}
