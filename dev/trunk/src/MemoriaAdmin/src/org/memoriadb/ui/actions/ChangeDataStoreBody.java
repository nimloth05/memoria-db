package org.memoriadb.ui.actions;

import java.awt.Frame;

import javax.swing.JOptionPane;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.model.Configuration;
import org.memoriadb.services.configuration.IDataStoreConfigurationService;
import org.memoriadb.services.store.IDatastoreService;
import org.memoriadb.ui.frames.ChoosDbDialog;
import org.memoriadb.ui.moodel.ConfigurationPM;

import com.google.inject.Inject;

public class ChangeDataStoreBody {
  
  private final IDataStoreConfigurationService fService;
  private final IDatastoreService fDataStoreService;

  @Inject
  public ChangeDataStoreBody(IDataStoreConfigurationService configurationService, IDatastoreService dataStoreService) {
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
      
      if (tryToChangeDb(configuration)) break;
    } while (true);
    
    fService.save(configuration);
  }

  private boolean tryToChangeDb(Configuration configuration) {
    try {
      fDataStoreService.change(configuration);
      return true;
    }
    catch (MemoriaException e) {
      JOptionPane.showMessageDialog((Frame)null, "The DB could not be opened. Reason: " + e.getLocalizedMessage(), "error", JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }

}
