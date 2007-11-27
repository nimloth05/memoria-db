package org.memoriadb.core.block;

import org.memoriadb.block.*;

/**
 * Additional methods for test purposes.
 * 
 * @author msc
 *
 */
public interface IBlockManagerExt extends IBlockManager {

  public Block getBlock(int index);
  
  public int getBlockCount();
  
  public int getRecyclingBlockCount();
  
}
