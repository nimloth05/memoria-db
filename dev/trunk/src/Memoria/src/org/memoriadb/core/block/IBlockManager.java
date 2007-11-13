package org.memoriadb.core.block;

import java.util.Set;

public interface IBlockManager {
  
  /**
   * Called when objects are read in.
   */
  public void add(Block block);
  
  /**
   * ATTENTION: The returned block may contain survivors!
   * 
   * Returns a block that maches the requirements or null, if a new block should be appended.
   * 
   * @param blockSize The net-size of the required block.
   * @return A block which has at least <tt>blockSize</tt> (the block still may contain survivors) 
   * or null, if no block met the requirements.  
   */
  public Block findRecyclebleBlock(long blockSize, Set<Block> tabooBlocks);

  /**
   * Called when the inactiveObjectDataRatio of a block changed.
   */
  public void inactiveRatioChanged(Block block);

}
