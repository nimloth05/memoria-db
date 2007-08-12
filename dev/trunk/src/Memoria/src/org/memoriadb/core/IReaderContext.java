package org.memoriadb.core;

import org.memoriadb.core.binder.IBindable;


public interface IReaderContext {

  public Object getObjectById(long objectId);

  public void objectToBind(IBindable bindable);

}
