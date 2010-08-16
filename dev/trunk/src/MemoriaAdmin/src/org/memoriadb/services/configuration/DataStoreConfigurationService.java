/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.services.configuration;

import org.memoriadb.CreateConfig;
import org.memoriadb.IObjectStore;
import org.memoriadb.Memoria;
import org.memoriadb.model.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataStoreConfigurationService implements IDataStoreConfigurationService {

  private IObjectStore fStore;

  @Override
  public void dispose() {
    getStore().close();
    fStore = null;
  }

  public IObjectStore getStore() {
    if (fStore == null) {
      createDB();
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
  
  private void createDB() {
    try {
      fStore = Memoria.open(new CreateConfig(), new File("config.db").getAbsolutePath());
    }
    catch (IOException e) {
      throw new RuntimeException("Could not open DB!", e);
    }
  }

}
