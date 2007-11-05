package org.memoriadb.core;

import java.util.*;

import org.java.patched.PIdentityHashMap;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.IdConstants;

/**
 * Holds the main-indexes. 
 * 
 * Objects can be added with no regard to ongoing transactions. The appropriate indexes are updated.
 * 
 * @author msc
 *
 */
public class ObjectRepo implements IObjectRepo {

  private long fCurrentObjectId = 0;
  
  /**
   * Holds all objectInfos for deleted objects. The reference to the Object in those ObjectInfos is always null. 
   */
  private final Map<Long, ObjectInfo> fDeletedMap = new HashMap<Long, ObjectInfo>();

  /**
   * Main-index
   */
  private final Map<Long, ObjectInfo> fIdMap = new HashMap<Long, ObjectInfo>();

  /**
   * Main-index
   */
  private final Map<Object, ObjectInfo> fObjectMap = new PIdentityHashMap<Object, ObjectInfo>();
  
  /**
   * MataClass index
   */
  private final Map<Class<?>, IMemoriaClassConfig> fMetaObjects = new HashMap<Class<?>, IMemoriaClassConfig>();

  /**
   * This method is only used for bootstrapping
   */
  public void add(long id, IMemoriaClass object) {
    internalPut(new ObjectInfo(id, object));
  }

  /**
   * Adds a new object to the repo. A new ObjectInfo is crated, with a new id and the version 0.
   * 
   * @return The new id
   */
  public long add(Object obj) {
    long result = generateId();
    internalPut(new ObjectInfo(result, obj));
    return result;
  }

  public void checkSanity() {
    for (Long id : fIdMap.keySet()) {
      Object object = fIdMap.get(id).getObj();

      long addressObjectId = fObjectMap.get(object).getId();
      if (id != addressObjectId) throw new MemoriaException("diffrent IDs for object: id in id-Map " + id + " id in adress map "
          + addressObjectId);
    }
  }

  public boolean contains(long id) {
    return fIdMap.containsKey(id);
  }

  public boolean contains(Object obj) {
    return fObjectMap.containsKey(obj);
  }

  @Override
  public long delete(Object obj) {
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

  /**
   * @return the metaObject for the given object or null, if the metaClass does not exists
   */
  public IMemoriaClassConfig getMemoriaClass(Class<?> klass) {
    if (klass.isArray()) return getGenericArrayMetaClass();
    return fMetaObjects.get(klass);
  }

  /**
   * 
   * @param objectId
   * @return the object for the given id or null.
   */
  public Object getObject(long objectId) {
    IObjectInfo objectInfo = fIdMap.get(objectId);
    if (objectInfo == null) throw new MemoriaException("No Object for ID: " + objectId);
    return objectInfo.getObj();
  }

  /**
   * @param obj
   * @return
   * @throws MemoriaException
   *           if object can not be found
   */
  public long getObjectId(Object obj) {
    IObjectInfo result = fObjectMap.get(obj);
    if (result == null) throw new MemoriaException("Unknown object: " + obj);
    return result.getId();
  }

  public ObjectInfo getObjectInfo(long id) {
    ObjectInfo result = fIdMap.get(id);
    if(result == null) result = fDeletedMap.get(id);
    return result;
  }

  public ObjectInfo getObjectInfo(Object obj) {
    return fObjectMap.get(obj);
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
  public boolean isMetaClass(Object obj) {
    return obj instanceof IMemoriaClass;
  }

  /**
   * @return true, if a MetaClass object exists for the given type.
   */
  public boolean metaClassExists(Class<?> klass) {
    if (klass.isArray()) throw new IllegalArgumentException("Array not expected");
    return fMetaObjects.containsKey(klass);
  }

  @Override
  public void objectDeleted(long id) {
    ObjectInfo info = fDeletedMap.get(id);
    internalUpdate(info);
  }
  
  public void objectUpdated(Object obj) {
    ObjectInfo info = fObjectMap.get(obj);
    internalUpdate(info);
  }

  private long generateId() {
    return ++fCurrentObjectId;
  }

  private IMemoriaClassConfig getGenericArrayMetaClass() {
    return (IMemoriaClassConfig) fIdMap.get(IdConstants.ARRAY_META_CLASS).getObj();
  }

  // TODO: Test for this assertions!
  private void internalPut(ObjectInfo info) {
    fCurrentObjectId = Math.max(fCurrentObjectId, info.getId());

    Object previousMapped = fObjectMap.put(info.getObj(), info);
    if (previousMapped != null) throw new MemoriaException("double registration in object-map" + info);

    previousMapped = fIdMap.put(info.getId(), info);
    if (previousMapped != null) throw new MemoriaException("double registration in id-Map" + info);

    if (info.getObj() instanceof IMemoriaClass) {
      IMemoriaClassConfig metaObject = (IMemoriaClassConfig) info.getObj();
      previousMapped = fMetaObjects.put(metaObject.getJavaClass(), metaObject);
      if (previousMapped != null) throw new MemoriaException("double registration of metaObject: " + metaObject);
    }
  }

  private void internalUpdate(ObjectInfo info) {
    if (info == null) throw new MemoriaException("Object not found: " + info);
    info.incrementVersion();
    info.incrememntOldGenerationCount();
  }

}
