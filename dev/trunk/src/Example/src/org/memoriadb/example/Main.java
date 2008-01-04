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
    IObjectStore store = Memoria.open(config, new File("test1").getAbsolutePath());
    
    store.save(new Person("Gandalf"));
    store.close();
  }

}
