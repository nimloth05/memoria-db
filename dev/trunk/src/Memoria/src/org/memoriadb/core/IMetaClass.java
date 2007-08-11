package org.memoriadb.core;

import org.memoriadb.core.handler.ISerializeHandler;

public interface IMetaClass {
  
  public static final long METACLASS_OBJECT_ID = 1;
  public static final long HANDLER_META_CLASS_OBJECT_ID = 2;
  public static final long ARRAY_META_CLASS = 3;

  public ISerializeHandler getHandler();

  public Class<?> getJavaClass();
  
  public Object newInstance();
  
}