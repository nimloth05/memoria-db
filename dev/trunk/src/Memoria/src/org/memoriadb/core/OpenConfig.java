package org.memoriadb.core;

import org.memoriadb.block.*;
import org.memoriadb.block.maintenancefree.MaintenanceFreeBlockManager;
import org.memoriadb.core.block.*;
import org.memoriadb.core.listener.*;

public class OpenConfig {
  
  private final IMemoriaListeners fListeners = new MemoriaListeners();
  private IBlockManager fBlockManager;

  public OpenConfig() {
    this(new MaintenanceFreeBlockManager());
  }
  
  public OpenConfig(IBlockManager blockManager) {
    fBlockManager = blockManager;
  }
  
  public IBlockManager getBlockManager() {
    return fBlockManager;
  }

  public IMemoriaListeners getListeners() {
    return fListeners;
  }
  
  public void setBlockManager(IBlockManager blockManager) {
    fBlockManager = blockManager;
  }
  
}