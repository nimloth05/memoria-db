package org.memoriadb;

import org.memoriadb.frames.MainFrame;
import org.memoriadb.services.store.*;

import com.google.inject.*;

public final class ApplicationController {

  private static Injector sInjector;

  public static void launch() {
    sInjector = configureServices();
    MainFrame mainFrame = sInjector.getInstance(MainFrame.class);
    mainFrame.show();
    
    boolean changed = sInjector.getInstance(IDatabaseService.class).change();
    if (!changed) shutdown();
  }

  private static Injector configureServices() {
    Injector injector = Guice.createInjector(new AbstractModule() {

      @Override
      protected void configure() {
        bind(IDatabaseService.class).to(DatabaseService.class); 
      }
    });
    return injector;
  }

  private static void shutdown() {
    sInjector.getInstance(IDatabaseService.class).close();
    System.exit(0);
  }


}
