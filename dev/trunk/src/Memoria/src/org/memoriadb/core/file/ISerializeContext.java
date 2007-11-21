package org.memoriadb.core.file;

import org.memoriadb.core.id.IObjectId;

public interface ISerializeContext {
  
  public boolean contains(Object obj);

  /**
   * @return ObjectId of the MemoriaClass representing the given java-class
   */
  public IObjectId getMemoriaClassId(String javaClassName);
  
  public IObjectId getNullReference();

  public IObjectId getObjectId(Object obj);
  
  public IObjectId getRootClassId();

}
