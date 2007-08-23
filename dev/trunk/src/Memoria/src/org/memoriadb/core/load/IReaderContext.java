package org.memoriadb.core.load;



public interface IReaderContext {

  public Object getObjectById(long objectId);

  public void objectToBind(IBindable bindable);

}
