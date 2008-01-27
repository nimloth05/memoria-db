package org.memoriadb.services.configuration;

import org.memoriadb.core.util.disposable.IDisposable;
import org.memoriadb.model.Configuration;

public interface IDataStoreConfigurationService extends IDisposable {
  
  public Configuration loadConfiguration();
  
  public void save(Configuration configuration);
  
}
