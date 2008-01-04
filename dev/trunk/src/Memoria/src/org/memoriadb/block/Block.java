package org.memoriadb.block;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.FileLayout;

/**
 * A Block can not change its position. It can not grow or shrink. It's data can just be moved to another block to
 * make this Block free for new data.
 *
 * The block header is only written once, when the block is appended. including:
 * - block-tag
 * - size
 * - crc over the size
 * 
 * Then follows the body, containing one transaction. The size is the size of the body!
 * 
 */
public class Block {
  
  /**
   * Bootstrapped and new objects refer to this block.
   */
  private static final Block sDefaultBlock = new Block(0,-1);
  
  /**
   * Number of bytes this block can store.
   * 
   * Limited to int because arrays can't be bigger than INT_MAX (implementation depends on arrays)
   */
  private long fBodySize;
  
  /**
   * Position in the file
   */
  private final long fPosition;
  private IBlockManager fManager;
  
  private long fObjectDataCount;
  private long fInactiveObjectDataCount;

  // if true, the inactiveRatio fo this block is 100%
  private boolean fIsFree = false;
  
  public static Block getDefaultBlock() {
    sDefaultBlock.setObjectDataCount(sDefaultBlock.getObjectDataCount()+1);
    return sDefaultBlock;
  }
  
  public Block(long position) {
    this(-1, position);
  }

  public Block(long bodySize, long position) {
    fBodySize = bodySize;
    fPosition = position;
    fObjectDataCount = 0;
    fInactiveObjectDataCount = 0;
  } 

  /**
   * @return The net-size of this block
   */
  public long getBodySize() {
    return fBodySize;
  }
  
  /**
   * @return The position, where the transaction starts (size of the transaction starts here)
   */
  public long getBodyStartPosition() {
    return getPosition() + FileLayout.BLOCK_HEADER_OVERHEAD;
  }

  public long getInactiveObjectDataCount() {
    return fInactiveObjectDataCount;
  }

  /**
   * @return A Value between 0 and 100. 0 Means: no inactive ObjectData, 100 means:
   * 100% of the ObjectData are inactive.
   */
  public long getInactiveRatio() {
    if(fIsFree) return 100;
    
    // when the block-size still is 0, the ratio is 0
    if(fObjectDataCount == 0) return 0;
    return fInactiveObjectDataCount*100 / fObjectDataCount;
  }

  public long getObjectDataCount() {
    return fObjectDataCount;
  }

  /**
   * @return The absolut position of this block.
   */
  public long getPosition() {
    return fPosition;
  }

  public long getWholeSize() {
    return fBodySize + FileLayout.BLOCK_OVERHEAD;
  }

  public void incrementInactiveObjectDataCount() {
    // FIXME experimental
    //if(fInactiveObjectDataCount == fObjectDataCount) return;
    
    ++fInactiveObjectDataCount;
    
    if(fInactiveObjectDataCount > fObjectDataCount) throw new MemoriaException(String.format("more inactive(%d) than active(%d) ObjectData", fInactiveObjectDataCount, fObjectDataCount));
    
    if(fManager != null)fManager.inactiveRatioChanged(this);
  }
  
  /**
   * Is called after all survivors were safed.
   */
  public void resetBlock(long numberOfObjects) {
    fInactiveObjectDataCount = 0;
    setObjectDataCount(numberOfObjects);
  }

  public void setBlockManager(IBlockManager manager) {
    fManager = manager;
  }
  
  public void setBodySize(long bodySize) {
    fBodySize = bodySize;
  }
  
  public void setIsFree() {
    fIsFree  = true;
  }

  public void setObjectDataCount(long numberOfObjects) {
    fObjectDataCount = numberOfObjects;
    if(fManager != null)fManager.inactiveRatioChanged(this);
  }

  /**
   * The size of this block (header + body)
   * @param blockSize
   */
  public void setWholeSize(long blockSize) {
    fBodySize = blockSize-FileLayout.BLOCK_OVERHEAD;
  }

  @Override
  public String toString() {
    return "Block ("+fInactiveObjectDataCount+"/"+fObjectDataCount+") pos:" + getPosition() + " size: " + getBodySize();
  }
  
}
