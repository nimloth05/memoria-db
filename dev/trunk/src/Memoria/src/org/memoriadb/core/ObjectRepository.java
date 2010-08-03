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

import java.util.*;

import org.memoriadb.block.Block;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.util.collection.identity.MemoriaIdentityHashMap;
import org.memoriadb.id.*;

/**
 * Holds the main-indexes.
 *
 * Objects can be added with no regard to ongoing transactions. The appropriate indexes are updated.
 *
 * @author sandro & msc
 *
 */
public class ObjectRepository implements IObjectRepository {

  /**
   * Holds all objectInfo for deleted objects. The reference to the Object in those ObjectInfo is always null.
   */
  private final Map<IObjectId, ObjectInfo> fDeletedMap = new HashMap<IObjectId, ObjectInfo>();

  /**
   * Main-index
   */
  private final Map<IObjectId, ObjectInfo> fIdMap = new HashMap<IObjectId, ObjectInfo>();

  /**
   * Main-index
   */
  private final Map<Object, ObjectInfo> fObjectMap = new MemoriaIdentityHashMap<Object, ObjectInfo>();

  /**
   * MataClass index
   */
  private final Map<String, IMemoriaClassConfig> fMemoriaClasses = new HashMap<String, IMemoriaClassConfig>();

  private final IObjectIdFactory fIdFactory;

  public ObjectRepository(IObjectIdFactory idFactory) {
    fIdFactory = idFactory;
  }

  /**
   * This method is only used for bootstrapping
   * @param id
   * @param id
   * @param object
   * @param object
   */
  public void add(IObjectId id, IMemoriaClass object) {
    ObjectInfo result = new ObjectInfo(id, object.getMemoriaClassId(), object, Block.getDefaultBlock());
    internalPut(result);
  }

  /**
   * Adds a new object to the repo. A new ObjectInfo is created, with a new id and the version 0.
   *
   * @return The ObjectInfo for the newly added Object.
   */
  @Override
  public ObjectInfo add(Object obj, IObjectId memoriaClassId) {
    IObjectId id = generateId();
    ObjectInfo result = new ObjectInfo(id, memoriaClassId, obj, Block.getDefaultBlock());
    internalPut(result);
    return result;
  }

  @Override
  public void checkIndexConsistency() {
    for (IObjectId id : fIdMap.keySet()) {
      Object object = fIdMap.get(id).getObject();

      IObjectId idInObjectMap = fObjectMap.get(object).getId();
      if (!id.equals(idInObjectMap)) throw new MemoriaException("different IDs for object: id in id-Map " + id + " id in address map "
          + idInObjectMap);
    }
  }

  @Override
  public boolean contains(IObjectId id) {
    return fIdMap.containsKey(id);
  }

  @Override
  public boolean contains(Object obj) {
    return fObjectMap.containsKey(obj);
  }

  @Override
  public IObjectInfo delete(Object obj) {
    ObjectInfo info = fObjectMap.remove(obj);
    if (info == null) throw new MemoriaException("object not found: " + obj);
    if (fIdMap.remove(info.getId()) == null) throw new MemoriaException("object not found: " + obj);
    fDeletedMap.put(info.getId(), info);
    info.setDeleted();
    return info;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterable<IMemoriaClass> getAllClasses() {
    Iterable<?> iterable = fMemoriaClasses.values();
    return (Iterable<IMemoriaClass>) iterable;
  }

  @Override
  public Collection<IObjectInfo> getAllObjectInfo() {
    return Collections.<IObjectInfo>unmodifiableCollection(fObjectMap.values());
  }

  @Override
  public Iterable<Object> getAllObjects() {
    return fObjectMap.keySet();
  }

  @Override
  public Iterable<Object> getAllUserSpaceObjects() {
    return new FilterMemoriaClassesIterable(getAllObjects());
  }

  @Override
  public IObjectId getExistingId(Object obj) {
    IObjectId result = getId(obj);
    if (result == null) throw new MemoriaException("Unknown object: "+ obj);
    return result;
  }

  @Override
  public Object getExistingObject(IObjectId id) {
    IObjectInfo objectInfo = fIdMap.get(id);
    if (objectInfo == null) throw new MemoriaException("Unknown id: " + id);
    return objectInfo.getObject();
  }

  /**
   * @param obj
   * @return
   * @throws MemoriaException
   *           if object can not be found
   */
  @Override
  public IObjectId getId(Object obj) {
    IObjectInfo result = fObjectMap.get(obj);
    if(result == null) return null;
    return result.getId();
  }

  @Override
  public IObjectIdFactory getIdFactory() {
    return fIdFactory;
  }

  @Override
  public IMemoriaClass getMemoriaClass(Object object) {
    IObjectInfo info = getObjectInfo(object);
    if(info == null) return null;
    return (IMemoriaClass) getObject(info.getMemoriaClassId());
  }

  /**
   * @return the metaObject for the given object or null, if the metaClass does not exists
   */
  @Override
  public IMemoriaClassConfig getMemoriaClass(String klass) {
    return fMemoriaClasses.get(klass);
  }

  /**
   *
   * @param objectId
   * @return the object for the given id or null.
   */
  @Override
  public Object getObject(IObjectId objectId) {
    IObjectInfo objectInfo = fIdMap.get(objectId);
    if (objectInfo == null) return null;
    return objectInfo.getObject();
  }

  @Override
  public ObjectInfo getObjectInfo(Object obj) {
    return fObjectMap.get(obj);
  }

  @Override
  public ObjectInfo getObjectInfoForId(IObjectId id) {
    ObjectInfo result = fIdMap.get(id);
    if(result == null) result = fDeletedMap.get(id);
    return result;
  }

  /**
   * Adds an object after dehydration
   * @param objectInfo
   * @param objectInfo
   */
  public void handleAdd(ObjectInfo objectInfo) {
    internalPut(objectInfo);
  }

  /**
   * marks an object as deleted after dehydrating the delete-marker
   * @param objectInfo
   */
  public void handleDelete(ObjectInfo objectInfo){
    fDeletedMap.put(objectInfo.getId(), objectInfo);
  }

  @Override
  public boolean isMemoriaClass(Object obj) {
    return obj instanceof IMemoriaClass;
  }

  public boolean isNullReference(IObjectId objectId) {
    return fIdFactory.isNullReference(objectId);
  }
  
  @Override
  public void removeFromIndex(IObjectInfo objectInfo) {
    if (!objectInfo.isDeleted() || !fDeletedMap.containsKey(objectInfo.getId())) throw new MemoriaException("object is not deleted"+objectInfo);
    fDeletedMap.remove(objectInfo.getId());
  }

  private IObjectId generateId() {
    return fIdFactory.createNextId();
  }

  private void internalPut(ObjectInfo info) {
    Object previousMapped = fObjectMap.put(info.getObject(), info);
    if (previousMapped != null) throw new MemoriaException("double registration in object-map " + info);

    previousMapped = fIdMap.put(info.getId(), info);
    if (previousMapped != null) throw new MemoriaException("double registration in id-Map " + info);

    if (info.getObject() instanceof IMemoriaClass) {
      IMemoriaClassConfig metaObject = (IMemoriaClassConfig) info.getObject();
      previousMapped = fMemoriaClasses.put(metaObject.getJavaClassName(), metaObject);
      if (previousMapped != null) throw new MemoriaException("double registration of memoria class: " + metaObject);
    }

 // adjustId here for bootstrapped objects
    fIdFactory.adjustId(info.getId());
  }

}
