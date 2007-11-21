package org.memoriadb.core.handler.def;


import java.util.Collection;

import org.memoriadb.core.handler.IDataObject;

public interface ICollectionDataObject extends IDataObject {
  
  public Collection<Object> getList();

}
