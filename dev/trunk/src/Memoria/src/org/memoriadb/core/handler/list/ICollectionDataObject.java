package org.memoriadb.core.handler.list;

import java.util.Collection;

import org.memoriadb.core.handler.IDataObject;

public interface ICollectionDataObject extends IDataObject {
  
  public Collection<Object> getCollection();

}
