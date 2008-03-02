package org.memoriadb.example;

import java.io.File;

import org.memoriadb.*;

public class Main {

  /**
   * @param args
   */
  public static void main(String[] args) {
    CreateConfig config = new CreateConfig();
    config.addCustomHandler(new PersonHandler());
    IObjectStore store = getStore(config);
    
    store.save(new Person("Gandalf"));
    store.close();
    
  }

  private static IObjectStore getStore(CreateConfig config) {
    IObjectStore store = Memoria.open(config, new File("test2").getAbsolutePath());
    return store;
  }

}
