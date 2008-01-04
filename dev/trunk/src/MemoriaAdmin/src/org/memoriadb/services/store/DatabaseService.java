package org.memoriadb.services.store;

import java.awt.Frame;

import javax.swing.JOptionPane;

import org.memoriadb.*;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.util.disposable.*;
import org.memoriadb.frames.ChoosDbDialog;
import org.memoriadb.model.Configuration;
import org.memoriadb.util.ClassPathManager;

import com.google.inject.Singleton;

@Singleton
public final class DatabaseService implements IDatabaseService {
  
  private final ListenerList<IChangeListener> fListeners = new ListenerList<IChangeListener>();
  private IDataStore fCurrentStore;
  private final ClassPathManager fManager = new ClassPathManager();

  @Override
  public IDisposable addListener(IChangeListener changeListener) {
    return fListeners.add(changeListener);
  }

  @Override
  public boolean change() {
    IDataStore dataStore = null;
    do {
      ChoosDbDialog chooseDbFrame = new ChoosDbDialog();
      Configuration configuration = chooseDbFrame.show();
      if (configuration.getDbPath().isEmpty()) return false;
      
      close();
      
      dataStore = tryOpenDB(configuration);
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
    fManager.resetClassLoader();
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

  private IDataStore tryOpenDB(Configuration configuration) {
    fManager.configure(configuration.getClassPaths());
    try {
      IDataStore dataStore = Memoria.openDataMode(new CreateConfig(), configuration.getDbPath());
      return dataStore;
    }
    catch (MemoriaException e) {
      JOptionPane.showMessageDialog((Frame)null, "The DB could not be opened. Reason: " + e.getLocalizedMessage(), "error", JOptionPane.ERROR_MESSAGE);
      return null;
    }
  }

  
}
