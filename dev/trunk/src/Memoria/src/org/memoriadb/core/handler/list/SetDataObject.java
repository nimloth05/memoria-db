package org.memoriadb.core.handler.list;

import java.util.*;

import org.memoriadb.core.id.IObjectId;

public class SetDataObject implements ISetDataObject {

  private final IObjectId fId;
  private final Set<Object> fSet;

  public SetDataObject(Set<Object> list, IObjectId id) {
    fSet = list;
    fId = id;
  }

  @Override
  public boolean equals(Object obj) {
    return fSet.equals(obj);
  }

  @Override
  public Collection<Object> getCollection() {
    return fSet;
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fId;
  }

  @Override
  public Set<Object> getSet() {
    return fSet;
  }

  @Override
  public int hashCode() {
    return fSet.hashCode();
  }

  @Override
  public String toString() {
    return fSet.toString();
  }
  
  

}
