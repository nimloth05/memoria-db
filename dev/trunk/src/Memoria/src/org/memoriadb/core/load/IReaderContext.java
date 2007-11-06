package org.memoriadb.core.load;

import java.io.DataInput;

import org.memoriadb.core.id.IObjectId;


public interface IReaderContext {

  public IObjectId createFrom(DataInput input);

  public Object getObjectById(IObjectId objectId);
  
  public boolean isRootClassId(IObjectId superClassId);

  public void objectToBind(IBindable bindable);

}
