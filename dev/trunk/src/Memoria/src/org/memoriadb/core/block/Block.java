package org.memoriadb.core.block;

import org.memoriadb.core.file.FileLayout;
import org.memoriadb.exception.MemoriaException;

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
  private static final Block sDefaultBlock = new Block(0,-1);
  
  /**
   * Number of bytes this block can store.
   * 
   * Limited to int because arrays can't be bigger than INT_MAX
   */
  private long fSize;
  
  /**
   * Position in the file
   */
  private final long fPosition;
  
  private int fObjectDataCount;
  
  private int fInactiveObjectDataCount;

  private IBlockManager fManager;
  
  public static Block getDefaultBlock() {
    sDefaultBlock.setNumberOfObjectData(sDefaultBlock.getObjectDataCount()+1);
    return sDefaultBlock;
  }
  
  public Block(long position) {
    this(-1, position);
  }

  public Block(long size, long position) {
    fSize = size;
    fPosition = position;
    fObjectDataCount = 0;
    fInactiveObjectDataCount = 0;
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
    if(fObjectDataCount == 0) return 0;
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
    if(fInactiveObjectDataCount > fObjectDataCount) throw new MemoriaException("more inactive("+fInactiveObjectDataCount+") than active("+fObjectDataCount+") ObjectData");
    if(fManager != null)fManager.inactiveRatioChanged(this);
  }

  public void setBlockManager(IBlockManager manager) {
    fManager = manager;
  }

  public void setNumberOfObjectData(int numberOfObjects) {
    fObjectDataCount = numberOfObjects;
    if(fManager != null)fManager.inactiveRatioChanged(this);
  }

  public void setSize(long blockSize) {
    fSize = blockSize;
  }

  @Override
  public String toString() {
    return "Block ("+fInactiveObjectDataCount+"/"+fObjectDataCount+") pos:" + getPosition() + " size: " + getSize();
  }
  
}
