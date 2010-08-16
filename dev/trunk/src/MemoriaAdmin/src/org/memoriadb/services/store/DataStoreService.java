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

package org.memoriadb.services.store;

import com.google.inject.Singleton;
import org.memoriadb.CreateConfig;
import org.memoriadb.IDataStore;
import org.memoriadb.Memoria;
import org.memoriadb.core.util.disposable.IDisposable;
import org.memoriadb.core.util.disposable.ListenerList;
import org.memoriadb.model.Configuration;
import org.memoriadb.util.ClassPathManager;

import java.io.File;
import java.io.IOException;

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

  @Override
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
