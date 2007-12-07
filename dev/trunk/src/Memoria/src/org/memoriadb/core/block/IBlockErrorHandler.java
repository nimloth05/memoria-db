package org.memoriadb.core.block;

import org.memoriadb.block.Block;

public interface IBlockErrorHandler {
  
  public long blockSizeCorrupt(Block block);
  
  public long blockTagCorrupt(Block block);
  
  public void transactionCorrupt(Block block);
  
}
