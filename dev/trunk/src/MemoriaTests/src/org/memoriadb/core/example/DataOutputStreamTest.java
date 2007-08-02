package org.memoriadb.core.example;

import java.io.File;
import java.util.*;

import org.java.patched.*;
import org.memoriadb.examples.Store;

import junit.framework.TestCase;

public class DataOutputStreamTest extends TestCase {
  
  @SuppressWarnings("nls")
  public void test_readWriteTypeData() throws Exception {
    File file = new File("test.txt");
    Store store = new Store(file);
    
    List<Class<?>> types = new ArrayList<Class<?>>();
    types.add(DataOutputStreamTest.class);
    types.add(Store.class);
    
    store.saveData(types);
    
    store = new Store(file);
    store.readData();
    Map<Long, String> storedTypes = store.getTypeMap();
    for(String className : storedTypes.values()) {
      assertTrue("could not found class", types.remove(Class.forName(className)));
    }
    assertTrue("not all classes were saved", types.isEmpty());
  }

}
