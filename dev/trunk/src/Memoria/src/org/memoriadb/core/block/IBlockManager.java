package org.memoriadb.core.block;

public interface IBlockManager {
  
  /**
   * Called when objects are read in.
   */
  public void add(Block block);
  
  /**
   * ATTENTION: The returned block may contained survivors!
   * 
   * @param blockSize The gross-size of the required block.
   * @return A block which has at least <tt>blockSize</tt>. The block still may contain survivors.
   */
  public Block getBlock(int blockSize);
  
}
