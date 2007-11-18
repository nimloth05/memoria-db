package org.memoriadb.core.handler.def;

import java.util.List;

import org.memoriadb.core.id.IObjectId;

public class ListDataObject implements IListDataObject {
  
  private final IObjectId fId;
  private final List<Object> fList;

  public ListDataObject(List<Object> list, IObjectId id) {
    fList = list;
    fId = id;
  }

  @Override
  public boolean equals(Object obj) {
    return fList.equals(obj);
  }

  @Override
  public List<Object> getList() {
    return fList;
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fId;
  }

  @Override
  public int hashCode() {
    return fList.hashCode();
  }

  @Override
  public String toString() {
    return fList.toString();
  }
  
  

}
