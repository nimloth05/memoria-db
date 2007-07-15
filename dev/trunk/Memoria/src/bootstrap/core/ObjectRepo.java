package bootstrap.core;

import java.util.*;

public class ObjectRepo {
  
  private long fObjectId;
  
  //Memory address of the object, ObjectId from the DB
  private Map<Integer, Long> fObjectToId = new HashMap<Integer, Long>();
  private Map<Long, Object> fIdToObject = new HashMap<Long, Object>();

  /**
   * 
   * 
   * 
   * @param object
   * @return the objectId for the given object. The object will be registered if not already registered.
   */
  public long register(Object object) {
    int address = System.identityHashCode(object);
    
    Long result = fObjectToId.get(address);
    
    if (result == null) {
      result = ++fObjectId;
      fObjectToId.put(address, result);
      fIdToObject.put(result, object);
    }
    return result;
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
  
}
