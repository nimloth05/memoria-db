package org.memoriadb.core.load;

import java.io.*;

import org.memoriadb.core.IDefaultInstantiator;
import org.memoriadb.core.id.IObjectId;


public interface IReaderContext {

  public IObjectId getArrayMemoriaClass();

  public IDefaultInstantiator getDefaultInstantiator();
  
  public Object getObjectById(IObjectId objectId);
  
  public IObjectId getPrimitiveClassId();

  public boolean isInDataMode();
  
  public boolean isNullReference(IObjectId objectId);

  public boolean isRootClassId(IObjectId superClassId);
  
  public void objectToBind(IBindable bindable);
  
  public IObjectId readObjectId(DataInput input) throws IOException;

}
