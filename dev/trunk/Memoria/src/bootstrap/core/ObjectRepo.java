package bootstrap.core;

import java.util.*;

import bootstrap.exception.MemoriaException;

public class ObjectRepo {
  
  private long fObjectId;
  
  //Memory address of the object, ObjectId from the DB
  private Map<Integer, Long> fObjectToId = new HashMap<Integer, Long>();
  private Map<Long, Object> fIdToObject = new HashMap<Long, Object>();

  /**
   * @param object
   * @return the objectId for the given object. The object will be registered if not already registered.
   */
  public long register(Object object) {
    Long result = internalGetObjectId(object);
    
    if (result == null) {
      result = ++fObjectId;
      internalPut(object, result);
    }
    return result;
  }

  private void internalPut(Object object, Long result) {
    fObjectToId.put(System.identityHashCode(object), result);
    fIdToObject.put(result, object);
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
    internalPut(obj, id);
  }
  
}
