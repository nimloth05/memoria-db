package org.memoriadb.handler.array;

import org.memoriadb.core.id.IObjectId;

public abstract class AbstractArray implements IArray {
  
  private final IObjectId fArrayClassId;

  /**
   * @param arrayClassId id of the generic array class
   */
  public AbstractArray(IObjectId arrayClassId) {
    fArrayClassId = arrayClassId;
  }
  
  public IObjectId getMemoriaClassId() {
    return fArrayClassId;
  }
  
}
