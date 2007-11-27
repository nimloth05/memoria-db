package org.memoriadb.handler.enu;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.IMemoriaClass;

public class EnumDataObject implements IEnumObject {
  
  private int fOrdinal;
  private final IObjectId fMemoriaClassId;
  
  public EnumDataObject(IObjectId memoriaClassId) {
    fMemoriaClassId = memoriaClassId;
  }

  public EnumDataObject(IObjectId memoriaClassId, int ordinal) {
    fMemoriaClassId = memoriaClassId;
    fOrdinal = ordinal;
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
  public int getOrdinal() {
    return fOrdinal;
  }

  @Override
  public void setOrdinal(int ordinal) {
    fOrdinal = ordinal;
  }
  
  
  
  

}
