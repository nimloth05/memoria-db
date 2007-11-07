package org.memoriadb.core.meta;

import org.memoriadb.core.handler.ISerializeHandler;

public interface IMemoriaClass {

  public ISerializeHandler getHandler();

  public Class<?> getJavaClass();
  
  public IMemoriaClass getSuperClass();

}