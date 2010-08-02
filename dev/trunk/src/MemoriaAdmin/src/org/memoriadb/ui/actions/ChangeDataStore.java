/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.ui.actions;

import com.google.inject.Inject;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.model.Configuration;
import org.memoriadb.services.configuration.IDataStoreConfigurationService;
import org.memoriadb.services.store.IDataStoreService;
import org.memoriadb.ui.frames.ChoosDbDialog;
import org.memoriadb.ui.moodel.ConfigurationPM;

import javax.swing.*;
import java.awt.*;

public class ChangeDataStore {
  
  private final IDataStoreConfigurationService fService;
  private final IDataStoreService fDataStoreService;

  @Inject
  public ChangeDataStore(IDataStoreConfigurationService configurationService, IDataStoreService dataStoreService) {
    fService = configurationService;
    fDataStoreService = dataStoreService;
  }
  
  public void run() {
    Configuration configuration = fService.loadConfiguration();
    ConfigurationPM configPM = ConfigurationPM.createFrom(configuration);
    
    do {
      ChoosDbDialog chooseDbFrame = new ChoosDbDialog(configPM);
      if (!chooseDbFrame.show()) return;

      configPM.applyTo(configuration);
      fService.save(configuration);
      
      if (tryToChangeDb(configuration)) break;
    } while (true);
    
  }

  private boolean tryToChangeDb(Configuration configuration) {
    try {
      fDataStoreService.change(configuration);
      return true;
    }
    catch (MemoriaException e) {
      JOptionPane.showMessageDialog(null, "The DB could not be opened. Reason: " + e.getLocalizedMessage(), "error", JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }

}
