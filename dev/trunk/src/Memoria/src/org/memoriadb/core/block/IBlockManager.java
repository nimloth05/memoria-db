package org.memoriadb.core.block;

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
   * @param blockSize The gross-size of the required block.
   * @return A block which has at least <tt>blockSize</tt> (the block still may contain survivors) 
   * or null, if no block met the requirements.  
   */
  public Block findRecyclebleBlock(int blockSize);
  

}
