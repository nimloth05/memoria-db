package org.memoriadb.core.meta;

import org.memoriadb.core.handler.ISerializeHandler;

public interface IMetaClass {

  public ISerializeHandler getHandler();

  public Class<?> getJavaClass();
  
  public IMetaClass getSuperClass();

  public Object newInstance();
  
}