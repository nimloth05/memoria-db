package org.memoriadb.core.file;

import org.memoriadb.core.id.IObjectId;

public interface ISerializeContext {
  
  /**
   * @return The objectId of the MetaClass for the given class-object.
   */
  public IObjectId getMemoriaClassId(Class<?> klazz);

  public IObjectId getObjectId(Object obj);

  public IObjectId getRootClassId();

}
