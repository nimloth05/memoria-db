package org.memoriadb.core.meta;

import org.memoriadb.core.handler.*;

public interface IMemoriaClass extends IDataObject {

  public ISerializeHandler getHandler();

  public String getJavaClassName();
  
  public IMemoriaClass getSuperClass();

  public boolean isTypeFor(String javaClass);

}