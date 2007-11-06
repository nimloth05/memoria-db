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
  private final long fSize;
  
  /**
   * Position in the file
   */
  private final long fPosition;
  
  public Block(long size, long position) {
    super();
    fSize = size;
    fPosition = position;
  }

  public long getPosition() {
    return fPosition;
  }

  public long getSize() {
    return fSize;
  }
  
  @Override
  public String toString() {
    return "Block @ " + getPosition() + " size " + getSize();
  }
  
}
