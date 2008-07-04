package org.memoriadb.services.store;

import java.io.*;

import org.memoriadb.*;
import org.memoriadb.core.util.disposable.*;
import org.memoriadb.model.Configuration;
import org.memoriadb.util.ClassPathManager;

import com.google.inject.Singleton;

@Singleton
public final class DataStoreService implements IDataStoreService {
  
  private final ListenerList<IChangeListener> fListeners = new ListenerList<IChangeListener>();
  private IDataStore fCurrentStore;
  private final ClassPathManager fManager = new ClassPathManager();

  @Override
  public IDisposable addListener(IChangeListener changeListener) {
    return fListeners.add(changeListener);
  }

  @Override
  public void change(Configuration configuration) {
    fManager.configure(configuration.getClasspathsAsURL());
    IDataStore dataStore = openStore(configuration);
    
    dispose();
      
    fCurrentStore = dataStore;
    notifyPostOpen();
  }

  public void dispose() {
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

  private IDataStore openStore(Configuration configuration) {
    try {
      IDataStore  dataStore = Memoria.openInDataMode(new CreateConfig(), new File(configuration.getDbPath()));
      return dataStore;
    }
    catch (IOException e) {
      throw new RuntimeException("DB-File could not be opened", e);
    }
  }

}
