package org.memoriadb.example;

import org.memoriadb.CreateConfig;
import org.memoriadb.IObjectStore;
import org.memoriadb.Memoria;

import java.io.File;
import java.io.IOException;

/**
 * Copyright
 */
public class Main {

  /**
   * @param args
   */
  public static void main(String[] args) {
    CreateConfig config = new CreateConfig();
    config.addCustomHandler(new PersonHandler());
    IObjectStore store = getStore(config);
    
    store.save(new Person("Gandalf"));
    store.beginUpdate();
    try {
      for (int i = 0; i < 1000; ++i) store.save(new City("Basel " + i));
    } 
    finally {
      store.endUpdate();
    }
    
    store.close();
    
  }

  private static IObjectStore getStore(CreateConfig config) {
    File file = new File("test2.db");
    System.out.println("path to db: " + file.getAbsolutePath());
    file.delete();
    try {
      return Memoria.open(config, file.getAbsolutePath());
    }
    catch(IOException  e) {
      throw new RuntimeException(e);
    }
  }

}
