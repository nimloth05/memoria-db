package org.memoriadb.core.load;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.id.IObjectId;


public interface IReaderContext {

  public IObjectId readObjectId(DataInput input) throws IOException;

  public IDefaultInstantiator getDefaultInstantiator();
  
  public DBMode getMode();

  public Object getObjectById(IObjectId objectId);
  
  public boolean isNullReference(IObjectId objectId);

  public boolean isRootClassId(IObjectId superClassId);
  
  public void objectToBind(IBindable bindable);

}
