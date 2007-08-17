package org.memoriadb.core;

import java.util.*;

import org.java.patched.PIdentityHashMap;
import org.memoriadb.exception.MemoriaException;


public class ObjectRepo {
  
  private long fCurrentObjectId = 0;
  
  private final Map<Long, Object> fIdToObject = new HashMap<Long, Object>();
  
  //We have to use the patched version of the IdentityHashMap.
  private final Map<Object, Long> fObjectToId = new PIdentityHashMap<Object, Long>();
  
  private final Map<Class<?>, IMetaClass> fMetaObjects = new HashMap<Class<?>, IMetaClass>();

  public void checkSanity() {
    for(Long id: fIdToObject.keySet()) {
      Object object = fIdToObject.get(id);
      
      long addressObjectId = internalGetObjectId(object);
      if (id != addressObjectId) throw new MemoriaException("diffrent IDs for object: id in id-Map "+ id + " id in adress map " + addressObjectId);
    }
  }
  
  public boolean contains(Object obj) {
    return internalGetObjectId(obj) != null;
  }
  
  public Collection<Object> getAllObjects() {
    return Collections.<Object>unmodifiableCollection(fIdToObject.values());
  }

  /**
   * 
   * @param javaType
   * @return the metaObject for the given java-Type or null.
   */
  public IMetaClass getMetaObject(Class<?> javaType) {
    if (javaType.isArray()) return (IMetaClass) fIdToObject.get(IMetaClass.ARRAY_META_CLASS);
    return fMetaObjects.get(javaType);
  }

  /**
   * 
   * @param objectId
   * @return the object for the given id or null.
   */
  public Object getObjectById(long objectId) {
    Object result = fIdToObject.get(objectId);
    return result;
  }

  public long getObjectId(Object obj) {
    Long result = internalGetObjectId(obj);
    if (result == null) throw new MemoriaException("Unknown object: " + obj);
    return result;
  }
  
  public Collection<Long> getObjects() {
    return Collections.unmodifiableSet(fIdToObject.keySet());
  }

  public void put(long id, Object obj) {
    fCurrentObjectId = Math.max(fCurrentObjectId, id);
    internalPut(obj, id);
  }

  
  /**
   * @param object
   * @return the objectId for the given object. The object will be registered if not already registered.
   */
  public long register(Object object) {
    Long result = internalGetObjectId(object);
    
    if (result == null) {
      result = ++fCurrentObjectId;
      internalPut(object, result);
    }
    return result;
  }
  
  private Long internalGetObjectId(Object obj) {
    return fObjectToId.get(obj);
  }
  
  private void internalPut(Object object, Long result) {
    if (object == null) throw new MemoriaException("Can not register null object, id: " + result);

    Object previousMapped = fObjectToId.put(object, result); 
    if (previousMapped != null) throw new RuntimeException("double registration in address-Map id" + result + " object: " + object);
    
    previousMapped = fIdToObject.put(result, object);
    if (previousMapped != null) throw new RuntimeException("double registration in objectId-Map id " + result + " object: " + object + " previous object " + previousMapped);
    
    if (object instanceof IMetaClass) {
      IMetaClass metaObject = (IMetaClass) object;
      fMetaObjects.put(metaObject.getJavaClass(), metaObject); 
    }
  }
  
}
