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

package org.memoriadb.core.mode;

import org.memoriadb.*;
import org.memoriadb.block.*;
import org.memoriadb.core.*;
import org.memoriadb.core.block.*;
import org.memoriadb.core.file.*;
import org.memoriadb.id.*;

/**
 * Abstract base class for a IStore implementation. 
 * This class has some additional extended functionality for test-purposes.
 * <b>It may be possible to corrupt the db when misusing the power of this functionality</b>.
 * 
 * @author sandro
 */
public abstract class AbstractStore implements IStore {
  
  protected final TransactionHandler fTransactionHandler;

  public AbstractStore(TransactionHandler handler) {
    fTransactionHandler = handler;
  }

  @Override
  public final void beginUpdate() {
    fTransactionHandler.beginUpdate();
  }

  public final void checkIndexConsistency() {
    fTransactionHandler.checkIndexConsistency();
  }

  @Override
  public final void close() {
    fTransactionHandler.close();
  }

  @Override
  public final boolean containsId(IObjectId id) {
    return fTransactionHandler.containsId(id);
  }

  @Override
  public final void endUpdate() {
    fTransactionHandler.endUpdate();
  }

  public final IBlockManager getBlockManager() {
    return fTransactionHandler.getBlockManager();
  }
  
  public final BlockRepository getBlockRepository() {
    return fTransactionHandler.getBlockRepository();
  }
  
  public final IMemoriaFile getFile() {
    return fTransactionHandler.getFile();
  }

  public final Header getHeader() {
    return fTransactionHandler.getHeader();
  }
  
  @Override
  public final long getHeadRevision() {
    return fTransactionHandler.getHeadRevision();
  }
  
  public final IIdProvider getIdFactory() {
    return fTransactionHandler.getDefaultIdProvider();
  }
  
  public final int getIdSize() {
    return fTransactionHandler.getIdSize();
  }
  
  /**
   * @return The stored ObjectInfo for the given object or null, if the given obj is unknown or deleted.
   */
  @Override
  public final IObjectInfo getObjectInfo(Object obj) {
    return fTransactionHandler.getObjectInfo(obj);
  }
  
  /**
   * @return The stored ObjectInfo for the given id or null, if the given id is unknown. This method may work
   * even for deleted objects, if the delete-marker is still present.
   */
  public final IObjectInfo getObjectInfoForId(IObjectId id) {
    return fTransactionHandler.getObjectInfoForId(id);
  }
  
  public final SurvivorAgent getSurvivorAgent(Block block) {
    return fTransactionHandler.getSurvivorAgent(block);
  }
  
  @Override
  public final ITypeInfo getTypeInfo() {
    return fTransactionHandler.getTypeInfo();
  }
  
  @Override
  public final boolean isInUpdateMode() {
    return fTransactionHandler.isInUpdateMode();
  }

}
