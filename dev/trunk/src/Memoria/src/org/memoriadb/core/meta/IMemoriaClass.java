package org.memoriadb.core.meta;

import org.memoriadb.core.handler.*;

public interface IMemoriaClass extends IDataObject {

  public IHandler getHandler();

  public String getJavaClassName();
  
  public IMemoriaClass getSuperClass();

  public boolean isTypeFor(String javaClass);

}