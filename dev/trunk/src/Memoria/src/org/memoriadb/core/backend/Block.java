package org.memoriadb.core.backend;

/**
 * A Block can not change its position. He can no grow or shrink. His data can just be moved to another block, and so
 * this block become free for use.  
 */
public class Block {
  
  private final long fSize;
  private final long fPosition;
  
  public Block(long size, long position) {
    fSize = size;
    fPosition = position;
  }

  public long getPosition() {
    return fPosition;
  }

  public long getSize() {
    return fSize;
  }
  
}
