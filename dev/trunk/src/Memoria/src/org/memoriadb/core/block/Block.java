package org.memoriadb.core.block;

import org.memoriadb.core.file.FileLayout;
import org.memoriadb.exception.MemoriaException;

/**
 * A Block can not change its position. It can not grow or shrink. It's data can just be moved to another block to
 * make this Block free for new data.
 * 
 * The crc32 check at the end fo the transaction-data includes the block's size, but not it's tag.
 */
public class Block implements Comparable<Block> {
  
  /**
   * Bootstrapped and new objects refer to this block.
   */
  public static final Block sVirtualBlock = new Block(0,-1);
  
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

  private final IBlockManager fManager;
  
  public Block(IBlockManager manager, long size, long position) {
    if(manager == null) throw new MemoriaException("BlockManager was null");
    
    fManager = manager;
    fSize = size;
    fPosition = position;
    fObjectDataCount = 0;
    fInactiveObjectDataCount = 0;
  }
  
  public Block(long size, long position) {
    this(BlockManagerDummy.INST, size, position);
  }

  @Override
  public int compareTo(Block o) {
    return (int)(getSize() - o.getSize());
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
   * @return A Value between 0 and 100. 0 Means: no inactive ObjectData, 100 means:
   * 100% of the ObjectData are inactive.
   */
  public long getInactiveRatio() {
    // when the block-size still is 0, the ratio is 0
    //if(fObjectDataCount == 0) return 0;
    return fInactiveObjectDataCount*100 / fObjectDataCount;
  }

  /**
   * @return The max number of bytes that can be stored in the contained transaction.
   */
  public long getMaxTrxDataSize() {
    return getSize() - FileLayout.TRX_OVERHEAD;
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

  public void incrementInactiveObjectDataCount() {
    ++fInactiveObjectDataCount;
    fManager.inactiveRatioChanged(this);
  }

  public void setNumberOfObjectData(int numberOfObjects) {
    fObjectDataCount = numberOfObjects;
    fManager.inactiveRatioChanged(this);
  }

  @Override
  public String toString() {
    return "Block ("+fInactiveObjectDataCount+"/"+fObjectDataCount+") pos:" + getPosition() + " size: " + getSize();
  }
  
}
