package org.memoriadb.core.handler.def;

import java.util.Collection;

import org.memoriadb.core.handler.def.ICollectionDataObject;
import org.memoriadb.core.id.IObjectId;

public class CollectionDataObject implements ICollectionDataObject {
  
  private final IObjectId fId;
  private final Collection<Object> fCollection;

  public CollectionDataObject(Collection<Object> list, IObjectId id) {
    fCollection = list;
    fId = id;
  }

  @Override
  public boolean equals(Object obj) {
    return fCollection.equals(obj);
  }

  @Override
  public Collection<Object> getList() {
    return fCollection;
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fId;
  }

  @Override
  public int hashCode() {
    return fCollection.hashCode();
  }

  @Override
  public String toString() {
    return fCollection.toString();
  }
  
  

}
