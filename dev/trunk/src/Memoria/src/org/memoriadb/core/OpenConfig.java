package org.memoriadb.core;

import org.memoriadb.core.block.*;

public class OpenConfig {
  
  private IBlockManager fBlockManager;
  private DBMode fDBMode;

  public OpenConfig() {
    this(new MaintenanceFreeBlockManager(), DBMode.clazz);
  }
  
  public OpenConfig(IBlockManager blockManager, DBMode mode) {
    fBlockManager = blockManager;
    fDBMode = mode;
  }

  public IBlockManager getBlockManager() {
    return fBlockManager;
  }

  public DBMode getDBMode() {
    return fDBMode;
  }
  
  public void setBlockManager(IBlockManager blockManager) {
    fBlockManager = blockManager;
  }
  

  public void setDBMode(DBMode mode) {
    fDBMode = mode;
  }

  
}