package org.memoriadb.core.handler.enu;

import org.memoriadb.core.handler.IDataObject;
import org.memoriadb.core.meta.IMemoriaClass;

public interface IEnumObject extends IDataObject {
  
  public Object getObject(IMemoriaClass memoriaClass);
  
  public int getOrdinal();

  public void setOrdinal(int ordinal);
  
  

}
