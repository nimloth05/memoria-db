package org.memoriadb.test.core.testclasses;

import org.memoriadb.core.handler.IDataObject;
import org.memoriadb.core.id.IObjectId;

public class DataObjectStub implements IDataObject {
  
  @Override
  public IObjectId getMemoriaClassId() {
    return null;
  }

}
