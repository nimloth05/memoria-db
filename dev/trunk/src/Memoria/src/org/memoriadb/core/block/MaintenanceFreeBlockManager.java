package org.memoriadb.core.block;

import java.util.*;

public class MaintenanceFreeBlockManager implements IBlockManagerExt {

  private final List<Block> fBlocks = new ArrayList<Block>();
  private final Set<Block> fReadyForCleanup = new HashSet<Block>();
    
  public MaintenanceFreeBlockManager() {
    
  }
  
  public void add(Block block) {
    fBlocks.add(block);
  }
  
  public Block findRecyclebleBlock(int blockSize) {
    return null;
  }
  
  public  Block getBlock(int index) {
    return fBlocks.get(index);
  }

  @Override
  public int getBlockCount() {
    return fBlocks.size();
  }

  public void incrementInactiveCount(Block block) {
    
  }

}
