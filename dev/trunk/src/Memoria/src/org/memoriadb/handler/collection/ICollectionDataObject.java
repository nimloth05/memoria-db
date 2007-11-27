package org.memoriadb.handler.collection;

import java.util.Collection;

import org.memoriadb.handler.IDataObject;

public interface ICollectionDataObject extends IDataObject {
  
  public Collection<Object> getCollection();

}
