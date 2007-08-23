package org.memoriadb.core;

import java.util.*;

import org.java.patched.PIdentityHashMap;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.MemoriaException;

public class ObjectRepo implements IObjectRepo {

  private long fCurrentObjectId = 0;

  private final Map<Long, ObjectInfo> fIdMap = new HashMap<Long, ObjectInfo>();

  // Use patched version of the IdentityHashMap.
  private final Map<Object, ObjectInfo> fObjectMap = new PIdentityHashMap<Object, ObjectInfo>();

  private final Map<Class<?>, IMetaClass> fMetaObjects = new HashMap<Class<?>, IMetaClass>();

  /**
   * Adds an object after dehydration
   */
  public void add(long id, Object obj, long version) {
    internalPut(new ObjectInfo(id, obj, version)); 
  } 
  
  /**
   * Adds a new object to the repo. A new ObjectInfo is crated, with a new id and the version 0.
   * @return The new id
   */ 
  public long add(Object obj) {
    long result = generateId();
    internalPut(new ObjectInfo(result,obj));
    
    return result;
  }

  public void addBootstrapped(long id, IMetaClass object) {
    internalPut(new ObjectInfo(id, object));
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

  /**
   * Creates a new MetaClass for the given <tt>obj</tt>. 
   */
  public IMetaClass createMetaClass(Class<?> klass) {
    if(klass.isArray()) throw new IllegalArgumentException("Array not expected");
    if(fMetaObjects.containsKey(klass)) throw new MemoriaException("MetaClass exists for " + klass);
    
    return new MetaClass(klass);
  }
  
  public Collection<Object> getAllObjects() {
    List<Object> result = new ArrayList<Object>(fObjectMap.size());
    for(ObjectInfo info: fObjectMap.values()){
      result.add(info.getObj());
    }
    return result;
  }
  
  /**
   * @return the metaObject for the given object or null, if the metaClass does not exists
   */
  public IMetaClass getMetaClass(Class<?> klass) {
    if (klass.isArray()) return getGenericArrayMetaClass();
    return fMetaObjects.get(klass);
  }

  /**
   * 
   * @param objectId
   * @return the object for the given id or null.
   */
  public Object getObject(long objectId) {
    ObjectInfo objectInfo = fIdMap.get(objectId);
    if (objectInfo == null) return null;
    return objectInfo.getObj();
  }

  /**
   * @param obj
   * @return
   * @throws MemoriaException if object can not be found
   */
  public long getObjectId(Object obj) {
    ObjectInfo result = fObjectMap.get(obj);
    if (result == null) throw new MemoriaException("Unknown object: " + obj);
    return result.getId();
  }

  public ObjectInfo getObjectInfo(Object obj) {
    return fObjectMap.get(obj);
  }

  /**
   * @return true, if a MetaClass object exists for the given type. 
   */
  public boolean metaClassExists(Class<?> klass) {
    if(klass.isArray()) throw new IllegalArgumentException("Array not expected");
    return fMetaObjects.containsKey(klass);
  }

  public void update(Object obj) {
    ObjectInfo info = getObjectInfo(obj);
    if(info == null) throw new MemoriaException("Object not found: " + obj);
    info.incrementVersion();
  }

  private long generateId() {
   return ++fCurrentObjectId; 
  }

  private IMetaClass getGenericArrayMetaClass() {
    return (IMetaClass) fIdMap.get(IMetaClass.ARRAY_META_CLASS).getObj();
  }

  private void internalPut(ObjectInfo info) {
    fCurrentObjectId = Math.max(fCurrentObjectId, info.getId());
    
    Object previousMapped = fObjectMap.put(info.getObj(), info); 
    if (previousMapped != null) throw new RuntimeException("double registration in object-map" + info);

    previousMapped = fIdMap.put(info.getId(), info);  
    if (previousMapped != null) throw new RuntimeException("double registration in id-Map" + info);

    if (info.getObj() instanceof IMetaClass) {
      IMetaClass metaObject = (IMetaClass) info.getObj();
      fMetaObjects.put(metaObject.getJavaClass(), metaObject);
    }
  }

}
