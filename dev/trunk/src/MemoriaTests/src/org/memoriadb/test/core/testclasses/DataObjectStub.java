package org.memoriadb.test.core.testclasses;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.handler.IDataObject;

public class DataObjectStub implements IDataObject {
  
  @Override
  public IObjectId getMemoriaClassId() {
    return null;
  }

}
