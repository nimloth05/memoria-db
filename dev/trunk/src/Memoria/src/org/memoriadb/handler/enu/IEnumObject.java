package org.memoriadb.handler.enu;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.IDataObject;

public interface IEnumObject extends IDataObject {

  public Object getObject(IMemoriaClass memoriaClass);
  
  public String getName();

  public void setName(String ordinal);

}