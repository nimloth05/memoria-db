package org.memoriadb.core.block;

/**
 * A Block can not change its position. He can not grow or shrink. His data can just be moved to another block, and so
 * this block become free for use.  
 */
public class Block {
  
  /**
   * Number of bytes this block can store.
   * 
   * Limited to int because arrays can't be bigger than INT_MAX
   */
  private final int fSize;
  private final long fPosition;
  private final boolean fCorrupt;
  
  public Block(int size, long position, boolean corrupt) {
    super();
    fSize = size;
    fPosition = position;
    fCorrupt = corrupt;
  }

  public long getPosition() {
    return fPosition;
  }

  public long getSize() {
    return fSize;
  }
  
}
