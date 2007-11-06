package org.memoriadb.core.id;

import java.io.*;

public interface IObjectIdFactory extends IDefaultObjectIdProvider {
  
  
  public IObjectId createFrom(DataInput input) throws IOException;

  public IObjectId createNextId();

}
