package org.memoriadb.test.testclasses;

import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.IObjectId;

public class DataObjectStub implements IDataObject {
  
  @Override
  public IObjectId getMemoriaClassId() {
    return null;
  }

}
