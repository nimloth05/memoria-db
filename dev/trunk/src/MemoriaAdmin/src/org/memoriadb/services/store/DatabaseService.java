package org.memoriadb.services.store;

import java.awt.Frame;

import javax.swing.JOptionPane;

import org.memoriadb.*;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.util.disposable.*;
import org.memoriadb.frames.ChoosDbDialog;

import com.google.inject.Singleton;

@Singleton
public final class DatabaseService implements IDatabaseService {
  
  private final ListenerList<IChangeListener> fListeners = new ListenerList<IChangeListener>();
  private IDataStore fCurrentStore;

  @Override
  public IDisposable addListener(IChangeListener changeListener) {
    return fListeners.add(changeListener);
  }

  @Override
  public boolean change() {
    IDataStore dataStore = null;
    do {
      ChoosDbDialog chooseDbFrame = new ChoosDbDialog();
      String dbPath = chooseDbFrame.show();
      if (dbPath.isEmpty()) return false;
      
      close();
      
      dataStore = tryOpenDB(dbPath);
    } while (dataStore == null);
    fCurrentStore = dataStore;
    notifyPostOpen();
    return true;
  }
  
  public void close() {
    if (fCurrentStore == null) return;
    notifyPreClose();
    fCurrentStore.close();
    fCurrentStore = null;
  }

  private void notifyPostOpen() {
    for(IChangeListener listener: fListeners) {
      listener.postOpen(fCurrentStore);
    }
  }

  private void notifyPreClose() {
    for(IChangeListener listener: fListeners) {
      listener.preClose();
    }
  }

  private IDataStore tryOpenDB(String dbPath) {
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
