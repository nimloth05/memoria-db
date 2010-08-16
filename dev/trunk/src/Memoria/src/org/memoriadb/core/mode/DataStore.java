/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.core.mode;

import java.util.List;

import org.memoriadb.*;
import org.memoriadb.block.Block;
import org.memoriadb.core.TransactionHandler;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.meta.IMemoriaClassConfig;
import org.memoriadb.core.query.DataModeQueryStrategy;
import org.memoriadb.core.refactor.RefactorApi;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.*;

/**
 * @author Sandro
 */
public class DataStore extends AbstractStore implements IDataStore {

  private final DataModeQueryStrategy fQueryStrategy = new DataModeQueryStrategy();

  public DataStore(TransactionHandler handler) {
    super(handler);
  }
  
  @Override
  public boolean contains(IDataObject obj) {
    if(obj == null) return false;
    return fTransactionHandler.contains(obj);
  }

  @Override
  public void delete(IDataObject obj) {
    if(obj == null) throw new MemoriaException("can not delete null");
    fTransactionHandler.delete(obj);
  }

  @Override
  public void deleteAll(IDataObject root) {
    if(root == null) throw new MemoriaException("can not delete null");
    fTransactionHandler.deleteAll(root);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends IDataObject> T  get(IObjectId id) {
    if(id== null) throw new MemoriaException("id was null");
    return (T) fTransactionHandler.getObject(id);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Iterable<IDataObject> getAllObjects() {
    Iterable<?> result= fTransactionHandler.getAllObjects();
    return (Iterable<IDataObject>) result;
  }

  @Override
  public IIdProvider getDefaultIdProvider() {
    return fTransactionHandler.getDefaultIdProvider();
  }

  @Override
  public IObjectId getId(IDataObject obj) {
    if(obj == null) throw new MemoriaException("null has no id");
    return fTransactionHandler.getId(obj);
  }

  @Override
  public IRefactor getRefactorApi() {
    return new RefactorApi(this); 
  }
  
  @Override
  public long getRevision(IObjectId id) {
    Block block = fTransactionHandler.getBlockRepository().getBlock(id);
    if (block == null) {
      return -1;
    }
    return block.getRevision();
  }
 
  public IMemoriaClassConfig internalGetMemoriaClass(String klass) {
    return fTransactionHandler.internalGetMemoriaClass(klass);
  }

  @Override
  public <T extends IDataObject> List<T> query(String clazz) {
    return fQueryStrategy.query(fTransactionHandler.getObjectRepo(), clazz);
  }

  @Override
  public <T extends IDataObject> List<T> query(String clazz, IFilter<T> filter) {
    return fQueryStrategy.query(fTransactionHandler.getObjectRepo(), clazz, filter);
  }

  @Override
  public IObjectId save(IDataObject obj) {
    if(obj == null) throw new MemoriaException("can not save null");
    return fTransactionHandler.save(obj);
  }

  @Override
  public IObjectId saveAll(IDataObject root) {
    if(root == null) throw new MemoriaException("can not save null");
    return fTransactionHandler.saveAll(root);
  }
  
  public void writePendingChanges() {
    fTransactionHandler.writePendingChanges();
  }

  void internalDelete(Object obj) {
    fTransactionHandler.internalDelete(obj);
  }
}
