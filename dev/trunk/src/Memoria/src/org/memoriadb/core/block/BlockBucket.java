package org.memoriadb.core.block;

import java.util.*;

public class BlockBucket implements Comparable<BlockBucket> {
  
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