package org.memoriadb.services.store;

import org.memoriadb.core.util.disposable.IDisposable;

public interface IDatabaseService {
  
  public IDisposable addListener(IChangeListener changeListener);

  public boolean change();

  public void close();

}
