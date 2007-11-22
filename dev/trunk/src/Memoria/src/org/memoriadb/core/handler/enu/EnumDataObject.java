package org.memoriadb.core.handler.enu;

import org.memoriadb.core.id.IObjectId;

public class EnumDataObject implements IEnumObject {
  
  private int fOrdinal;
  private IObjectId fMemoriaClassId;
  
  public EnumDataObject(IObjectId memoriaClassId, int ordinal) {
    
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fMemoriaClassId;
  }

}
