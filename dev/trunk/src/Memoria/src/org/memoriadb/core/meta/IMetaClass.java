package org.memoriadb.core.meta;

import org.memoriadb.core.handler.ISerializeHandler;

public interface IMetaClass {

  /**
   * The MetaClass for the Java Object.class is not saved in the database. Its a bootstrap class.
   */
  public static final long JAVA_OBJECT_META_OBJECT_ID = 4;
  
  public static final long METACLASS_OBJECT_ID = 1;
  public static final long HANDLER_META_CLASS_OBJECT_ID = 2;
  public static final long ARRAY_META_CLASS = 3;
  
  //The ID for a MetaClass which does not have a superClass.
  public static final long NO_SUPER_CLASS_ID = -1;

  public ISerializeHandler getHandler();

  public Class<?> getJavaClass();
  
  public IMetaClass getSuperClass();

  public Object newInstance();
  
}