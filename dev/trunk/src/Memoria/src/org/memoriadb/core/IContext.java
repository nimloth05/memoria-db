package org.memoriadb.core;

import org.memoriadb.exception.MemoriaException;


public interface IContext {
  
  public boolean contains(Object referencee);
  

  /**
   * @return The object or null
   */
  public Object getObject(long objectId);
  
  /**
   * 
   * @param obj
   * @throws MemoriaException if the object was not found.
   * @return
   */
  public long getObjectId(Object obj);
  
}
