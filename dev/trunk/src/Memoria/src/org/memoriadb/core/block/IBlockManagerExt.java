package org.memoriadb.core.block;

/**
 * Additional methods for test purposes.
 * 
 * @author msc
 *
 */
public interface IBlockManagerExt extends IBlockManager {

  public Block getBlock(int index);
  
  public int getBlockCount();
  
}
