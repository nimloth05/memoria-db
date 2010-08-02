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

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.id.IObjectId;

import java.util.List;

/**
 * Facade to a memoria DB
 * 
 * @author Sandro Orlando
 * 
 */
public interface IObjectStore extends IStore {


  public boolean contains(Object obj);
  
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
  public void delete(Object obj);

  /**
   * Removes the given object-graph from this ObjectStore. 
   *
   * References to the deleted objects remain untouched for the current object graph, but will be null
   * when the file is opened with a new ObjectStore.
   * 
   * If the given object is not contained in this store, nothing will happen.
   * 
   * @param obj
   * @param root
   * @param root
   */
  public void deleteAll(Object root);

  /**
   * @param id
   * @return The object or null, if no Object exists for the given id. It is not considered if the object is persistent
   *         or not.
   * @param id
   */
  public <T> T get(IObjectId id);

  /**
   * 
   * @return all Objects in the Memoria-Store.
   */
  public Iterable<Object> getAllObjects();
  
  /**
   * 
   * @return all Objects in the Memoria-Store <b>except</b> Memoria-Classes.
   */
  public Iterable<Object> getAllUserSpaceObjects();

  /**
   * @param obj
   * @return The objectId of the given object.
   * @throws MemoriaException
   *           If the given object can not be found.
   * @param obj
   */
  public IObjectId getId(Object obj);
  
  public <T> List<T> query(Class<T> clazz);

  public <FILTER, T extends FILTER> List<T> query(Class<T> clazz, IFilter<FILTER> filter);
  
  public <T> List<T> query(String clazz);
  
  public List<Object> query(String clazz, IFilter<Object> filter);
  
  /**
   * Adds the given object to the store or performs an update if the given object is already contained.
   * 
   * Changes are immediately written to the persistent store, except this ObjectStore is in 
   * UpdateMode. in this case, changes are batched until <tt>endUpdate()</tt> is called. 
   * 
   * @param obj
   * @return The objectId of the added or updated object.
   * @param obj
   */
  public IObjectId save(Object obj);
  
  /**
   * Saves the given <tt>root</tt> object and all referenced objects.
   * 
   * Changes are immediately written to the persistent store, except this ObjectStore is in 
   * UpdateMode. in this case, changes are batched until <tt>endUpdate()</tt> is called. 
   * 
   * @param root
   * @return objectId of the given <tt>root</tt> object.
   * @param root
   */
  public IObjectId saveAll(Object root);

}
