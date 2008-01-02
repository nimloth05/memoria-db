package org.memoriadb.services.store;

import org.memoriadb.IDataStore;

public interface IChangeListener {

  public void preClose();
  
  public void postOpen(IDataStore newStore);

}
