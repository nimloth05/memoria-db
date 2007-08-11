package org.memoriadb.core;


public interface IReaderContext {

  public Object getObjectById(long objectId);

  public void objectToBind(IBindable bindable);

}
