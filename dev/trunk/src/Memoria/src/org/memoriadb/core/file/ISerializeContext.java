package org.memoriadb.core.file;

public interface ISerializeContext {
  
  /**
   * @return The objectId of the MetaClass for the given class-object.
   */
  public long getMetaClassId(Class<?> klazz);

  public long getObjectId(Object obj);

}