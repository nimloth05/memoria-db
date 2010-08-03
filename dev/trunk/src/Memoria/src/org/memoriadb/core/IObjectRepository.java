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

package org.memoriadb.core;

import java.util.Collection;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.meta.*;
import org.memoriadb.id.*;

/**
 * This interfaces encapsulates the indexes to the ObjectInfo. The methods are only intended 
 * for normal use, i.e. after bootstrapping and loading from file.
 * 
 * @author sandro
 *
 */
public interface IObjectRepository {

  /**
   * Adds an object to the container. A new objectId is generated.
   * @param obj
   * @param obj
   * @param memoriaClassId
   * @param memoriaClassId
   * @pre The given obj is not already in the container.
   * @return The newly generated id.
   */
  public ObjectInfo add(Object obj, IObjectId memoriaClassId);

  
  public void checkIndexConsistency();
  
  public boolean contains(IObjectId id);
  
  public boolean contains(Object obj);
  
  /**
   * Called when an object is deleted in the same transaction as it was added.
   * @param obj object to delete.
   * @return
   */
  public IObjectInfo delete(Object obj);
  
  public Iterable<IMemoriaClass> getAllClasses();

  public Collection<IObjectInfo> getAllObjectInfo();

  /**
   * @return All objects in the repository, including all bootstrapped or class-objects
   */
  public Iterable<Object> getAllObjects();
  
  public Iterable<Object> getAllUserSpaceObjects();

  public Iterable<Object> getAllUserSpaceObjects(Class<?> clazz);

  public IObjectId getExistingId(Object obj);

  public Object getExistingObject(IObjectId id);
  
  /**
   * @param obj
   * @return The objectId of the given object.
   * @throws MemoriaException If the given object can not be found.
   * @param obj
   */
  public IObjectId getId(Object obj);

  public IObjectIdFactory getIdFactory();

  /**
   * @param object
   * @return the memoria-class for the given obejct (works NOT for value-objects).
   * @param object
   */
  public IMemoriaClass getMemoriaClass(Object object);
  
  /**
   * @param klass full qualified class name. 
   * @return The MetaClass for the given java-type. Array-Metaclass is the given <tt>klass</tt>
   * is an array.
   * @throws MemoriaException if no MetaClass can be found
   */
  public IMemoriaClassConfig getMemoriaClass(String klass);


  /**
   * @param id
   * @return The object for the given Id even if the transaction is not completed.
   *         <code>null</code> if the object was not found.
   * @param id
   */
  public Object getObject(IObjectId id);


  /**
   * @param obj
   * @return The stored ObjectInfo for the given object or null, if the given obj is unknown or deleted.
   * @param obj
   */
  public ObjectInfo getObjectInfo(Object obj);

  /**
   * @param id
   * @return The stored ObjectInfo for the given id or null, if the given id is unknown. This method may work
   * even for deleted objects, if the delete-marker is still present.
   * @param id
   */
  public ObjectInfo getObjectInfoForId(IObjectId id);


  /**
   * @param obj
   * @return true, if the given obj is a metaclass
   * @param obj
   */
  public boolean isMemoriaClass(Object obj);
  
  /**
   * Removes an object completely from the index. The object must previously have been deleted.
   * @param objectInfo
   */
  public void removeFromIndex(IObjectInfo objectInfo);

}
