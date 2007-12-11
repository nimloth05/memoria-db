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
