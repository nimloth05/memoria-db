package org.memoriadb;

import org.memoriadb.services.configuration.*;
import org.memoriadb.services.store.*;
import org.memoriadb.ui.actions.ChangeDataStore;
import org.memoriadb.ui.frames.MainFrame;

import com.google.inject.*;

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
