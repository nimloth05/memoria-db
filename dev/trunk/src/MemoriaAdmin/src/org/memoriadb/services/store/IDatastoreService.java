package org.memoriadb.services.store;

import org.memoriadb.core.util.disposable.IDisposable;
import org.memoriadb.model.Configuration;

public interface IDatastoreService extends IDisposable {
  
  public IDisposable addListener(IChangeListener changeListener);

  public void change(Configuration configuration);

}
