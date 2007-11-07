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

  /**
   * @return The max number of bytes that can be stored in the contained transaction.
   */
  public long getMaxTrxDataSize() {
    return getSize() - BlockLayout.TRX_OVERHEAD;
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
  public String toString() {
    return "Block @ " + getPosition() + " size " + getSize();
  }
  
}
