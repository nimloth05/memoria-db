package org.memoriadb.core.block;

import java.util.*;

public class MaintenanceFreeBlockManager implements IBlockManagerExt {

  // mindestens 50% der Objekte müssen inactive sein....
  private final double fCleanUpThreshold = 0.5;
  
  // die Neue Size muss mindestens 50% des platzes benötigen..
  private final double fSizeThreshold = 0.5;
  
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
