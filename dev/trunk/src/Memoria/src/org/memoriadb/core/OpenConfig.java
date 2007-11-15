package org.memoriadb.core;

import org.memoriadb.core.block.*;
import org.memoriadb.core.listener.*;

public class OpenConfig {
  
  private final IMemoriaListeners fListeners = new MemoriaListeners();
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

  public IMemoriaListeners getListeners() {
    return fListeners;
  }
  
  public void setBlockManager(IBlockManager blockManager) {
    fBlockManager = blockManager;
  }
  

  public void setDBMode(DBMode mode) {
    fDBMode = mode;
  }

  
}