package org.memoriadb.core.file;

public class LastWrittenBlockInfo {
  private final int fWriteMode;
  private final long fPosition;

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
    return fPosition != FileHeaderHelper.NO_CURRENT_BLOCK;
  }

  /**
   * @return true, if the last written block was appended to the file.
   */
  public boolean isAppend() {
    return fWriteMode == FileLayout.WRITE_MODE_APPEND;
  }
  
}
