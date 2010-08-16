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

import org.memoriadb.IFilter;
import org.memoriadb.IObjectStore;
import org.memoriadb.core.TransactionHandler;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.query.ObjectModeQueryStrategy;
import org.memoriadb.id.IObjectId;

import java.util.List;

/**
 * @author Sandro
 */
public class ObjectStore extends AbstractStore implements IObjectStore  {

  private final ObjectModeQueryStrategy fQueryStrategy = new ObjectModeQueryStrategy();

  public ObjectStore(TransactionHandler handler) {
    super(handler);
  }
  
  @Override
  public boolean contains(Object obj) {
    if(obj == null) return false;
    return fTransactionHandler.contains(obj);
  }

  @Override
  public void delete(Object obj) {
    if(obj == null) throw new MemoriaException("can not delete null");
    fTransactionHandler.delete(obj);
  }

  @Override
  public void deleteAll(Object root) {
    if(root == null) throw new MemoriaException("can not delete null");
    fTransactionHandler.deleteAll(root);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T get(IObjectId id) {
    if(id== null) throw new MemoriaException("id was null");
    return (T) fTransactionHandler.getObject(id);
  }

  @Override
  public Iterable<Object> getAllObjects() {
    return fTransactionHandler.getAllObjects();
  }

  @Override
  public Iterable<Object> getAllUserSpaceObjects() {
    return fTransactionHandler.getAllUserSpaceObjects();
  }

  @Override
  public IObjectId getId(Object obj) {
    if(obj == null) throw new MemoriaException("null has no id");
    return fTransactionHandler.getId(obj);
  }

  @Override
  public <T> List<T> query(Class<T> clazz) {
    return fQueryStrategy.query(fTransactionHandler.getObjectRepo(), clazz);
  }

  @Override
  public <FILTER, T extends FILTER> List<T> query(Class<T> clazz, IFilter<FILTER> filter) {
    return fQueryStrategy.query(fTransactionHandler.getObjectRepo(), clazz, filter);
  }
  
  @Override
  public <T> List<T> query(String clazz) {
    return fQueryStrategy.query(fTransactionHandler.getObjectRepo(), clazz);
  }
  
  @Override
  public List<Object> query(String clazz, IFilter<Object> filter) {
    return fQueryStrategy.query(fTransactionHandler.getObjectRepo(), clazz);
  }
  
  @Override
  public IObjectId save(Object obj) {
    if(obj == null) throw new MemoriaException("can not save null");
    return fTransactionHandler.save(obj);
  }

  @Override
  public IObjectId saveAll(Object root) {
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
