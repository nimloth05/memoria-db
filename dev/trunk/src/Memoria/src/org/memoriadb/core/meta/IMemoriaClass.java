package org.memoriadb.core.meta;

import org.memoriadb.core.handler.ISerializeHandler;

public interface IMemoriaClass {

  public ISerializeHandler getHandler();

  public String getJavaClassName();
  
  public IMemoriaClass getSuperClass();

  public boolean isTypeFor(String javaClass);

}