package org.memoriadb.core.listener;

import org.memoriadb.core.block.Block;

public interface IWriteListener {

  public void afterAppend(Block block);
  public void afterWrite(Block block);
  
  public void beforeAppend(Block block);
  public void beforeWrite(Block block);

}
