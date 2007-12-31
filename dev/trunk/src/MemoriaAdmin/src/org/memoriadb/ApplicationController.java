package org.memoriadb;

import java.awt.Frame;

import javax.swing.JOptionPane;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.frames.*;

public final class ApplicationController {

  public static void launch() {
    IDataStore dataStore = openDB();

    MainFrame mainFrame = new MainFrame(dataStore);
    mainFrame.show();
  }

  private static IDataStore openDB() {
    IDataStore dataStore = null;
    do {
      ChoosDbDialog chooseDbFrame = new ChoosDbDialog();
      String dbPath = chooseDbFrame.show();
      if (dbPath.isEmpty()) shutdown();
      
      dataStore = tryOpenDB(dbPath);
    }
    while (dataStore == null);
    return dataStore;
  }

  private static void shutdown() {
    System.exit(0);
  }

  private static IDataStore tryOpenDB(String dbPath) {
    try {
      IDataStore dataStore = Memoria.openDataMode(new CreateConfig(), dbPath);
      return dataStore;
    }
    catch (MemoriaException e) {
      JOptionPane.showMessageDialog((Frame)null, "The DB could not be opened. Reason: " + e.getLocalizedMessage(), "error", JOptionPane.ERROR_MESSAGE);
      return null;
    }
  }

}
