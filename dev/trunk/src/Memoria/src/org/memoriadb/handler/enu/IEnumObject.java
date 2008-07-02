package org.memoriadb.handler.enu;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.IDataObject;

public interface IEnumObject extends IDataObject {

  public Object getObject(IMemoriaClass memoriaClass);
  
  public int getOrdinal();

  public void setOrdinal(int ordinal);

}