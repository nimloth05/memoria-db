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

import java.util.List;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.*;

/**
 * Facade to a memoria DB
 * 
 * @author Sandro Orlando
 * 
 */
public interface IDataStore extends IStore {

  public boolean contains(IDataObject obj);

  /**
   * Removes the given object from this ObjectStore. Removed objects can later be added again, resulting in a new id.
   * 
   * References to the given object remain untouched for the current object graph, but will be null
   * when the file is opened with a new ObjectStore.
   * 
   * If the given object is not contained in this store, nothing will happen.
   * 
   * @param obj Object which is deleted.
   */
  public void delete(IDataObject obj);

  /**
   * Removes the given object-graph from this ObjectStore. 
   *
   * References to the deleted objects remain untouched for the current object graph, but will be null
   * when the file is opened with a new ObjectStore.
   * 
   * If the given object is not contained in this store, nothing will happen.
   * 
   * @param obj
   */
  public void deleteAll(IDataObject root);

  /**
   * @return The object or null, if no Object exists for the given id. It is not considered if the object is persistent
   *         or not.
   */
  public <T extends IDataObject> T  get(IObjectId id);
  
  public Iterable<IDataObject> getAllObjects();
  
  public IIdProvider getDefaultIdProvider();
  
  /**
   * @return The objectId of the given object.
   * @throws MemoriaException
   *           If the given object can not be found.
   */
  public IObjectId getId(IDataObject obj);
  
  public IRefactor getRefactorApi();
  
  public long getRevision(IObjectId id);
  
  public <T extends IDataObject> List<T> query(String clazz);
  
  public <T extends IDataObject> List<T> query(String clazz, IFilter<T> filter);

  /**
   * Adds the given object to the store or performs an update if the given object is already contained.
   * 
   * Changes are immediately written to the persistent store, except this ObjectStore is in 
   * UpdateMode. in this case, changes are batched until <tt>endUpdate()</tt> is called. 
   * 
   * @return The objectId of the added or updated object.
   */
  public IObjectId save(IDataObject obj);

  /**
   * Saves the given <tt>root</tt> object and all referenced objects.
   * 
   * Changes are immediately written to the persistent store, except this ObjectStore is in 
   * UpdateMode. in this case, changes are batched until <tt>endUpdate()</tt> is called. 
   * 
   * @return objectId of the given <tt>root</tt> object.
   */
  public IObjectId saveAll(IDataObject root);

}
