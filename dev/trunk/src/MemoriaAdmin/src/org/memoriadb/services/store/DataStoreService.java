package org.memoriadb.services.store;

import org.memoriadb.*;
import org.memoriadb.core.util.disposable.*;
import org.memoriadb.model.Configuration;
import org.memoriadb.util.ClassPathManager;

import com.google.inject.Singleton;

@Singleton
public final class DataStoreService implements IDatastoreService {
  
  private final ListenerList<IChangeListener> fListeners = new ListenerList<IChangeListener>();
  private IDataStore fCurrentStore;
  private final ClassPathManager fManager = new ClassPathManager();

  @Override
  public IDisposable addListener(IChangeListener changeListener) {
    return fListeners.add(changeListener);
  }

  @Override
  public void change(Configuration configuration) {
    fManager.configure(configuration.getClassPaths());
    IDataStore  dataStore = Memoria.openInDataMode(new CreateConfig(), configuration.getDbPath());
    
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

}
