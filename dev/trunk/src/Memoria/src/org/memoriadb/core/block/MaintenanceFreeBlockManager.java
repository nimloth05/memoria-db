package org.memoriadb.core.block;

import java.util.*;

public class MaintenanceFreeBlockManager implements IBlockManagerExt {

  private final int fInactiveThreshold;
  
  private final int fSizeThreshold;
  
  private final List<Block> fBlocks = new ArrayList<Block>();
  
  private final TreeSet<Block> fRecycleList = new TreeSet<Block>();
    
  public MaintenanceFreeBlockManager() {
    // default-value
    this(50, 50);
  }
  
  /**
   * 
   * @param inactiveThreshold 0..100%. 0 means: at leat on ObjectData must be inactive. 
   *        100 means: all (100%) of the ObjectDatas must be inactive
   * 
   * @param sizeThreshold 0..100%: 0 means: every block qualifies for recycling, 100% menas: A block only 
   *        qualifies for recycling when it's size matches the requested size.
   */
  public MaintenanceFreeBlockManager(int inactiveThreshold, int sizeThreshold) {
    fInactiveThreshold= inactiveThreshold;
    fSizeThreshold = sizeThreshold;
  }
  
  public void add(Block block) {
    fBlocks.add(block);
  }
  
  public Block findRecyclebleBlock(long blockSize) {
    // create a block as search-prototype
    Block block = fRecycleList.ceiling(new Block(blockSize, -1));
    
    if(block==null) return null; // no block with the requested size in the recycle list.
    
    long ratio = blockSize*100/block.getSize();
    if(ratio >= fSizeThreshold) return block;
    
    // the found block is too big for the requested size.
    return null;
  }
  
  public  Block getBlock(int index) {
    return fBlocks.get(index);
  }

  @Override
  public int getBlockCount() {
    return fBlocks.size();
  }
  
  public int getRecyclingBlockCount() {
    return fRecycleList.size();
  }

  @Override
  public void inactiveRatioChanged(Block block) {
    if(blockQualifiesForRecycling(block)) fRecycleList.add(block);
  }

  private boolean blockQualifiesForRecycling(Block block) {
    // if the inactiveThreshold is 0, a single inactive ObjectData qualifies the block for recycling.
    if(fInactiveThreshold == 0) return block.getInactiveObjectDataCount()>0;
    
    return block.getInactiveRatio() >= fInactiveThreshold;
  }

}
