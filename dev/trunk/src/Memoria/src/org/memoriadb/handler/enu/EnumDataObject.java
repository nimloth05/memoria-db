package org.memoriadb.handler.enu;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.id.IObjectId;

public class EnumDataObject implements IEnumObject {
  
  private String fName;
  private final IObjectId fMemoriaClassId;

  public EnumDataObject(IObjectId memoriaClassId) {
    fMemoriaClassId = memoriaClassId;
  }

  public EnumDataObject(IObjectId memoriaClassId, String name) {
    fMemoriaClassId = memoriaClassId;
    fName = name;
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fMemoriaClassId;
  }

  @Override
  public Object getObject(IMemoriaClass memoriaClass) {
    return this;
  }

  @Override
  public String getName() {
    return fName;
  }

  @Override
  public void setName(String name) {
    fName = name;
  }

}
