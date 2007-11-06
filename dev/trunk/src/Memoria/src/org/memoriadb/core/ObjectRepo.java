package org.memoriadb.core;

import java.util.*;

import org.java.patched.PIdentityHashMap;
import org.memoriadb.core.id.*;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.MemoriaException;

/**
 * Holds the main-indexes. 
 * 
 * Objects can be added with no regard to ongoing transactions. The appropriate indexes are updated.
 * 
 * @author msc
 *
 */
public class ObjectRepo implements IObjectRepo {

  /**
   * Holds all objectInfos for deleted objects. The reference to the Object in those ObjectInfos is always null. 
   */
  private final Map<IObjectId, ObjectInfo> fDeletedMap = new HashMap<IObjectId, ObjectInfo>();

  /**
   * Main-index
   */
  private final Map<IObjectId, ObjectInfo> fIdMap = new HashMap<IObjectId, ObjectInfo>();

  /**
   * Main-index
   */
  private final Map<Object, ObjectInfo> fObjectMap = new PIdentityHashMap<Object, ObjectInfo>();
  
  /**
   * MataClass index
   */
  private final Map<Class<?>, IMemoriaClassConfig> fMemoriaClasses = new HashMap<Class<?>, IMemoriaClassConfig>();
  
  private final IObjectIdFactory fIdFactory;
  

  public ObjectRepo(IObjectIdFactory idFactory) {
    fIdFactory = idFactory;
  }

  /**
   * This method is only used for bootstrapping
   */
  public void add(IObjectId id, IMemoriaClass object) {
    internalPut(new ObjectInfo(id, object));
  }

  /**
   * Adds a new object to the repo. A new ObjectInfo is crated, with a new id and the version 0.
   * 
   * @return The new id
   */
  public IObjectId add(Object obj) {
    IObjectId result = generateId();
    internalPut(new ObjectInfo(result, obj));
    return result;
  }

  public void checkSanity() {
    for (IObjectId id : fIdMap.keySet()) {
      Object object = fIdMap.get(id).getObj();

      IObjectId idInObjectMap = fObjectMap.get(object).getId();
      if (!id.equals(idInObjectMap)) throw new MemoriaException("diffrent IDs for object: id in id-Map " + id + " id in adress map "
          + idInObjectMap);
    }
  }

  public boolean contains(IObjectId id) {
    return fIdMap.containsKey(id);
  }

  public boolean contains(Object obj) {
    return fObjectMap.containsKey(obj);
  }

  @Override
  public IObjectId delete(Object obj) {
    ObjectInfo info = fObjectMap.remove(obj);
    if (info == null) throw new MemoriaException("object not found: " + obj);
    if (fIdMap.remove(info.getId()) == null) throw new MemoriaException("object not found: " + obj);
    fDeletedMap.put(info.getId(), info);
    info.setDeleted();
    return info.getId();
  }

  public Collection<Object> getAllObjects() {
    List<Object> result = new ArrayList<Object>(fObjectMap.size());
    for (IObjectInfo info : fObjectMap.values()) {
      result.add(info.getObj());
    }
    return result;
  }

  @Override
  public IObjectId getArrayMemoriaClass() {
    return fIdFactory.getArrayMemoriaClass();    
  }

  @Override
  public IObjectId getHandlerMetaClass() {
    return fIdFactory.getHandlerMetaClass();
  }

  @Override
  public IObjectIdFactory getIdFactory() {
    return fIdFactory;
  }

  /**
   * @return the metaObject for the given object or null, if the metaClass does not exists
   */
  public IMemoriaClassConfig getMemoriaClass(Class<?> klass) {
    if (klass.isArray()) return getGenericArrayMetaClass();
    return fMemoriaClasses.get(klass);
  }

  @Override
  public IObjectId getMemoriaClassDeletionMarker() {
    return fIdFactory.getMemoriaClassDeletionMarker();
  }

  @Override
  public IObjectId getMemoriaMetaClass() {
    return fIdFactory.getMemoriaMetaClass();
  }
  
  /**
   * 
   * @param objectId
   * @return the object for the given id or null.
   */
  public Object getObject(IObjectId objectId) {
    IObjectInfo objectInfo = fIdMap.get(objectId);
    if (objectInfo == null) throw new MemoriaException("No Object for ID: " + objectId);
    return objectInfo.getObj();
  }

  @Override
  public IObjectId getObjectDeletionMarker() {
    return fIdFactory.getObjectDeletionMarker();
  }

  /**
   * @param obj
   * @return
   * @throws MemoriaException
   *           if object can not be found
   */
  public IObjectId getObjectId(Object obj) {
    IObjectInfo result = fObjectMap.get(obj);
    if (result == null) throw new MemoriaException("Unknown object: " + obj);
    return result.getId();
  }

  public ObjectInfo getObjectInfo(IObjectId id) {
    ObjectInfo result = fIdMap.get(id);
    if(result == null) result = fDeletedMap.get(id);
    return result;
  }
  
  public ObjectInfo getObjectInfo(Object obj) {
    return fObjectMap.get(obj);
  }

  @Override
  public IObjectId getRootClassId() {
    return fIdFactory.getRootClassId();
  }

  /**
   * Adds an object after dehydration
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
  public boolean isMemoriaClassDeletionMarker(IObjectId typeId) {
    return fIdFactory.isMemoriaClassDeletionMarker(typeId);
  }

  @Override
  public boolean isMemoriaMetaClass(IObjectId typeId) {
    return fIdFactory.isMemoriaMetaClass(typeId);
  }

  @Override
  public boolean isMetaClass(Object obj) {
    return obj instanceof IMemoriaClass;
  }

  @Override
  public boolean isObjectDeletionMarker(IObjectId typeId) {
    return fIdFactory.isObjectDeletionMarker(typeId);
  }

  @Override
  public boolean isRootClassId(IObjectId superClassId) {
    return fIdFactory.isRootClassId(superClassId);
  }

  /**
   * @return true, if a MetaClass object exists for the given type.
   */
  public boolean metaClassExists(Class<?> klass) {
    if (klass.isArray()) throw new IllegalArgumentException("Array not expected");
    return fMemoriaClasses.containsKey(klass);
  }

  @Override
  public void objectDeleted(IObjectId id) {
    ObjectInfo info = fDeletedMap.get(id);
    internalUpdate(info);
  }

  public void objectUpdated(Object obj) {
    ObjectInfo info = fObjectMap.get(obj);
    internalUpdate(info);
  }

  private IObjectId generateId() {
    return fIdFactory.createNextId();
  }

  private IMemoriaClassConfig getGenericArrayMetaClass() {
    return (IMemoriaClassConfig) fIdMap.get(fIdFactory.getArrayMemoriaClass()).getObj();
  }

  // TODO: Test for this assertions!
  private void internalPut(ObjectInfo info) {
    fIdFactory.adjustId(info.getId());
    
    Object previousMapped = fObjectMap.put(info.getObj(), info);
    if (previousMapped != null) throw new MemoriaException("double registration in object-map " + info);

    previousMapped = fIdMap.put(info.getId(), info);
    if (previousMapped != null) throw new MemoriaException("double registration in id-Map " + info);

    if (info.getObj() instanceof IMemoriaClass) {
      IMemoriaClassConfig metaObject = (IMemoriaClassConfig) info.getObj();
      previousMapped = fMemoriaClasses.put(metaObject.getJavaClass(), metaObject);
      if (previousMapped != null) throw new MemoriaException("double registration of memoria class: " + metaObject);
    }
  }

  private void internalUpdate(ObjectInfo info) {
    if (info == null) throw new MemoriaException("Object not found: " + info);
    info.incrementVersion();
    info.incrememntOldGenerationCount();
  }

}
