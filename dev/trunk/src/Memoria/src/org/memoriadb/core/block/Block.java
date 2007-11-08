package org.memoriadb.core.block;

import org.memoriadb.core.file.BlockLayout;

/**
 * A Block can not change its position. It can not grow or shrink. It's data can just be moved to another block to
 * make this Block free for new data.
 * 
 * The crc32 check at the end fo the transaction-data includes the block's size, but not it's tag.
 */
public class Block {
  
  /**
   * Bootstrapped and new objects refer to this block.
   */
  public static final Block sVirtualBlock = new Block(0,0);
  
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
  
  private int fObjectDataCount;
  
  private int fInactiveObjectDataCount;
  
  public Block(long size, long position) {
    super();
    fSize = size;
    fPosition = position;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final Block other = (Block) obj;
    if (fInactiveObjectDataCount != other.fInactiveObjectDataCount) return false;
    if (fObjectDataCount != other.fObjectDataCount) return false;
    if (fPosition != other.fPosition) return false;
    if (fSize != other.fSize) return false;
    return true;
  }

  public int getInactiveObjectDataCount() {
    return fInactiveObjectDataCount;
  }
  
  /**
   * @return The max number of bytes that can be stored in the contained transaction.
   */
  public long getMaxTrxDataSize() {
    return getSize() - BlockLayout.TRX_OVERHEAD;
  }
  
  public int getObjectDataCount() {
    return fObjectDataCount;
  }

  public long getPosition() {
    return fPosition;
  }

  /**
   * @return The net-size of this block
   */
  public long getSize() {
    return fSize;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + fInactiveObjectDataCount;
    result = prime * result + fObjectDataCount;
    result = prime * result + (int) (fPosition ^ (fPosition >>> 32));
    result = prime * result + (int) (fSize ^ (fSize >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "Block pos:" + getPosition() + " size: " + getSize();
  }
  
  
  
}
