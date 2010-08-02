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

package org.memoriadb;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.memoriadb.services.configuration.DataStoreConfigurationService;
import org.memoriadb.services.configuration.IDataStoreConfigurationService;
import org.memoriadb.services.store.DataStoreService;
import org.memoriadb.services.store.IDataStoreService;
import org.memoriadb.ui.actions.ChangeDataStore;
import org.memoriadb.ui.frames.MainFrame;

public final class ApplicationController {

  private static Injector sInjector;

  public static void launch() {
    sInjector = prepareService();
    
    MainFrame mainFrame = sInjector.getInstance(MainFrame.class);
    mainFrame.show();
    
    sInjector.getInstance(ChangeDataStore.class).run();
  }

  public static void shutdown() {
    sInjector.getInstance(IDataStoreService.class).dispose();
    sInjector.getInstance(IDataStoreConfigurationService.class).dispose();
    System.exit(0);
  }

  private static Injector prepareService() {
    Injector injector = Guice.createInjector(new AbstractModule() {

      @Override
      protected void configure() {
        bind(IDataStoreService.class).to(DataStoreService.class);
        bind(IDataStoreConfigurationService.class).to(DataStoreConfigurationService.class);
      }
    });
    return injector;
  }

}
