package org.memoriadb.core;

import java.io.File;
import java.util.*;

import org.java.patched.*;

import junit.framework.TestCase;

public abstract class AbstractFileStoreTest extends TestCase {
  
  protected IObjectContainer fStore;
  
  private File fFile;
  
  protected final void createStore() {
    fStore = Memoria.open(fFile);
  }
  
  protected final <T> List<T> getAll(Class<T> clazz) {
    List<T> result = new ArrayList<T>();
    for(Object object: fStore.getAllObjects()) {
      if (clazz.isInstance(object)) result.add(clazz.cast(object));
    }
    return result;
  }
  
  protected final void reopen() {
    createStore(); 
  }

  protected final List<Object> save(Object...objects) {
    List<Object> list = Arrays.asList(objects);
    fStore.writeObject(list);
    return list;
  }
  
  @Override
  protected void setUp() {
    fFile = new File("fileStore.db");
    fFile.delete(); 
    createStore();
  }

}
