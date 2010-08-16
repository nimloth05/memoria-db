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

package org.memoriadb.example.simple;

import org.memoriadb.CreateConfig;
import org.memoriadb.IObjectStore;
import org.memoriadb.Memoria;
import org.memoriadb.example.classes.City;
import org.memoriadb.example.classes.Person;

import java.io.File;
import java.io.IOException;

/**
 * We save some objects in the databse. No special configuration is performed.
 *
 * @author Sandro
 */
public class Main {

  public static void main(String[] args) {
    CreateConfig config = new CreateConfig();
    
    IObjectStore store = openNewStore(config);
    saveObjects(store);
    store.close();
  }

  private static void saveObjects(final IObjectStore store) {
    //single Transaction
    store.save(new Person("Merlin"));

    //multiple updates in one transaction
    store.beginUpdate();
    try {
      for (int i = 0; i < 10; ++i) store.save(new City("Avalon " + i));
    }
    finally {
      store.endUpdate();
    }
  }

  private static IObjectStore openNewStore(CreateConfig config) {
    File file = new File("simple.db");
    System.out.println("path to db: " + file.getAbsolutePath());
    if (file.exists()) {
      if (!file.delete()) throw new IllegalStateException("could not delete old db");
    }
    try {
      return Memoria.open(config, file.getAbsolutePath());
    }
    catch(IOException  e) {
      throw new RuntimeException(e);
    }
  }

}