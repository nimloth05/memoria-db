package org.memoriadb.id;

import java.io.*;


public interface IObjectIdFactory extends IIdProvider {
  
  public void adjustId(IObjectId id);

  public IObjectId createFrom(DataInput input) throws IOException;
  
  public IObjectId createNextId();
  

}
