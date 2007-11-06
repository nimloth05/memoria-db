package org.memoriadb.core.block;

public interface IBlockManager {
  
  /**
   * Called when objects are read in.
   */
  public void add(Block block);
}
