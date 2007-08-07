package org.memoriadb.core;

import org.memoriadb.exception.MemoriaException;


public interface IContext {
  
  public boolean contains(Object referencee);
  

  public MetaClass getMetaObject(Class<?> javaType);
  
  public Object getObjectById(long objectId);
  
  /**
   * 
   * @param obj
   * @throws MemoriaException if the object was not found.
   * @return
   */
  public long getObjectId(Object obj);

  public void put(long objectId, Object obj);

  public long register(Object object);
  
}