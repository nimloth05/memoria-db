package org.memoriadb.core.handler.def;

import java.util.List;

import org.memoriadb.core.handler.IDataObject;

public interface IListDataObject extends IDataObject {
  
  public List<Object> getList();

}
