package bootstrap.core;

import java.util.*;

import bootstrap.exception.MemoriaException;

public class ObjectRepo {
  
  private long fCurrentObjectId = 0;
  
  //Memory address of the object, ObjectId from the DB
  private Map<Integer, Long> fObjectToId = new HashMap<Integer, Long>();
  private Map<Long, Object> fIdToObject = new HashMap<Long, Object>();
  
  private Map<Class<?>, MetaClass> fMetaObjects = new HashMap<Class<?>, MetaClass>();

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

  private void internalPut(Object object, Long result) {
    if (object == null) throw new MemoriaException("Can not register null object, id: " + result);
    fObjectToId.put(System.identityHashCode(object), result);
    fIdToObject.put(result, object);
    
    if (object instanceof MetaClass) {
      MetaClass metaObject = (MetaClass) object;
      fMetaObjects.put(metaObject.getJavaClass(), metaObject); 
    }
  }
  
  private Long internalGetObjectId(Object obj) {
    int address = System.identityHashCode(obj);
    return fObjectToId.get(address);
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
  
  public void put(long id, Object obj) {
    fCurrentObjectId = Math.max(fCurrentObjectId, id);
    internalPut(obj, id);
  }

  /**
   * 
   * @param javaType
   * @return the metaObject for the given java-Type or null.
   */
  public MetaClass getMetaObject(Class<?> javaType) {
    return fMetaObjects.get(javaType);
  }

  public Collection<MetaClass> getMetaObejcts() {
    return Collections.unmodifiableCollection(fMetaObjects.values());
  }
  
  public boolean contains(Object obj) {
    return internalGetObjectId(obj) != null;
  }

  public Collection<Long> getObjects() {
    return Collections.unmodifiableSet(fIdToObject.keySet());
  }
  
}
