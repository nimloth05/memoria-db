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

package org.memoriadb.example;

import org.memoriadb.CreateConfig;
import org.memoriadb.IObjectStore;
import org.memoriadb.Memoria;
import org.memoriadb.example.classes.City;
import org.memoriadb.example.classes.Person;

import java.io.File;
import java.io.IOException;

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
