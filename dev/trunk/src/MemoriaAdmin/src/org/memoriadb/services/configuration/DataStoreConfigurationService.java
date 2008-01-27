package org.memoriadb.services.configuration;

import java.io.File;
import java.util.List;

import org.memoriadb.*;
import org.memoriadb.model.Configuration;

public class DataStoreConfigurationService implements IDataStoreConfigurationService {

  private IObjectStore fStore;

  @Override
  public void dispose() {
    getStore().close();
    fStore = null;
  }

  public IObjectStore getStore() {
    if (fStore == null) {
      fStore = Memoria.open(new CreateConfig(), new File("config.db").getAbsolutePath());
    }
    return fStore;
  }

  @Override
  public Configuration loadConfiguration() {
    List<Configuration> result = getStore().query(Configuration.class);
    if (result.size() > 1) throw new IllegalStateException("Es können nicht mehrere Configurations gespeichert werden");
    if (result.size() == 1) return result.get(0);
    
    return createAndSaveConfiguraiton();
  }

  @Override
  public void save(Configuration configuration) {
    if (!getStore().contains(configuration)) throw new RuntimeException("Dieses API unterstützt keine neuen Configuration, es muss zuerst über loadConfig bezogen werden");
    getStore().saveAll(configuration);
  }
  
  private Configuration createAndSaveConfiguraiton() {
    Configuration configuration = new Configuration();
    getStore().saveAll(configuration);
    
    return configuration;
  }

}
