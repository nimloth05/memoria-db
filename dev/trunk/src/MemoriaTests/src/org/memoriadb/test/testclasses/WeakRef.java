package org.memoriadb.test.testclasses;

import org.memoriadb.id.IObjectId;

public class WeakRef {
  
  private IObjectId fObjectId;

  public IObjectId getObjectId() {
    return fObjectId;
  }

  public void setObjectId(IObjectId objectId) {
    fObjectId = objectId;
  }

}