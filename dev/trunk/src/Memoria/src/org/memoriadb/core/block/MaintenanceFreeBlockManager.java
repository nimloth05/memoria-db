package org.memoriadb.core.block;

import java.util.*;

import org.memoriadb.exception.MemoriaException;

public class MaintenanceFreeBlockManager implements IBlockManagerExt {

  private static class BlockBucket implements Comparable<BlockBucket> {
    
    private final long fSize;
    private List<Block> fBlocks;
    
    public BlockBucket(long size) {
      fSize = size;
    }
    
    public void add(Block block) {
      // optimization to avoid instantiation of the List in case when no Blocks are added (search-prototype)
      if(fBlocks == null) fBlocks = new ArrayList<Block>();
      
      fBlocks.add(block);
    }
    
    @Override
    public int compareTo(BlockBucket o) {
      return (int)(fSize - o.fSize);
    }

    public int getBlockCount() {
      return fBlocks.size();
    }

    public long getSize() {
      return fSize;
    }

    public boolean isEmpty() {
      return fBlocks.isEmpty();
    }

    public Block pop() {
      return fBlocks.remove(0);
    }
    
  }
  
  private final int fInactiveThreshold;
  
  private final int fSizeThreshold;
  
  private final List<Block> fBlocks = new ArrayList<Block>();
    
  private final TreeSet<BlockBucket> fRecycleList = new TreeSet<BlockBucket>();
  
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
    checkIsPercent(inactiveThreshold);
    checkIsPercent(sizeThreshold);
    
    fInactiveThreshold= inactiveThreshold;
    fSizeThreshold = sizeThreshold;
  }
  
  public void add(Block block) {
    fBlocks.add(block);
    block.setBlockManager(this); 
    inactiveRatioChanged(block);
  }

  public Block findRecyclebleBlock(long blockSize) {
    // create a block as search-prototype
    BlockBucket blockBucket = fRecycleList.ceiling(new BlockBucket(blockSize));
    
    if(blockBucket==null) return null; // no block with the requested size in the recycle list.
    if(blockBucket.isEmpty()){
      fRecycleList.remove(blockBucket);
      return null;
    }
    
    long ratio = blockSize*100/blockBucket.getSize();
    if(ratio >= fSizeThreshold){
      return blockBucket.pop();
    }
    
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
    int result = 0;
    for(BlockBucket bucket: fRecycleList){
      result += bucket.getBlockCount();
    }
    return result;
  }
  
  @Override
  public void inactiveRatioChanged(Block block) {
    if(!blockQualifiesForRecycling(block)) return;
    
    BlockBucket prototype = new BlockBucket(block.getSize());
    BlockBucket bucket = fRecycleList.ceiling(prototype);
    if(bucket == null || bucket.getSize()!=block.getSize()) {
      bucket = prototype;
      fRecycleList.add(bucket);
    }
    
    bucket.add(block);
  }

  private boolean blockQualifiesForRecycling(Block block) {
    // if the inactiveThreshold is 0, a single inactive ObjectData qualifies the block for recycling.
    if(fInactiveThreshold == 0) return block.getInactiveObjectDataCount()>0;
    
    return block.getInactiveRatio() >= fInactiveThreshold;
  }

  private void checkIsPercent(int value) {
    if(value<0 || value>100 ) throw new MemoriaException("not in [0..100]:" + value);
  }

}
