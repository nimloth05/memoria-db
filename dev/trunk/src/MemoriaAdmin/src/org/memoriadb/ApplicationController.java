package org.memoriadb;

import org.memoriadb.services.configuration.*;
import org.memoriadb.services.store.*;
import org.memoriadb.ui.actions.ChangeDataStoreBody;
import org.memoriadb.ui.frames.MainFrame;

import com.google.inject.*;

public final class ApplicationController {

  private static Injector sInjector;

  public static void launch() {
    sInjector = prepareService();
    
    MainFrame mainFrame = sInjector.getInstance(MainFrame.class);
    mainFrame.show();
    
    sInjector.getInstance(ChangeDataStoreBody.class).run();
  }

  private static Injector prepareService() {
    Injector injector = Guice.createInjector(new AbstractModule() {

      @Override
      protected void configure() {
        bind(IDatastoreService.class).to(DataStoreService.class);
        bind(IDataStoreConfigurationService.class).to(DataStoreConfigurationService.class);
      }
    });
    return injector;
  }

  private static void shutdown() {
    sInjector.getInstance(IDatastoreService.class).dispose();
    sInjector.getInstance(IDataStoreConfigurationService.class).dispose();
    System.exit(0);
  }

}
