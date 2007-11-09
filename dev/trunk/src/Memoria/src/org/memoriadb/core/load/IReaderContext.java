package org.memoriadb.core.load;

import java.io.*;

import org.memoriadb.core.DBMode;
import org.memoriadb.core.id.IObjectId;


public interface IReaderContext {

  public IObjectId createFrom(DataInput input) throws IOException;

  public DBMode getMode();
  
  public Object getObjectById(IObjectId objectId);

  public boolean isNullReference(IObjectId objectId);
  
  public boolean isRootClassId(IObjectId superClassId);

  public void objectToBind(IBindable bindable);

}
