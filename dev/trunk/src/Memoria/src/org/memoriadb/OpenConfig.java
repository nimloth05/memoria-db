/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb;

import org.memoriadb.block.IBlockManager;
import org.memoriadb.block.maintenancefree.MaintenanceFreeBlockManager;
import org.memoriadb.core.listener.IMemoriaListeners;
import org.memoriadb.core.listener.MemoriaListeners;

//FIXME API Doc
public class OpenConfig {
  
  private final IMemoriaListeners fListeners = new MemoriaListeners();
  private IBlockManager fBlockManager;

  public OpenConfig() {
    this(new MaintenanceFreeBlockManager(70, 80));
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