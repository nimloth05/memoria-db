package org.memoriadb.test.core;

import java.util.*;

import junit.framework.TestCase;

import org.memoriadb.core.facade.*;

public abstract class AbstractObjectStoreTest extends TestCase {
  
  protected IMemoria fStore;
  
  protected final void createStore() {
    fStore = MemoriaFactory.open();
  }
  
  protected final <T> List<T> getAll(Class<T> clazz) {
    List<T> result = new ArrayList<T>();
    for(Object object: fStore.getAllObjects()) {
      if (clazz.isInstance(object)) result.add(clazz.cast(object));
    }
    return result;
  }
  
  protected final void recreateStore() {
    fStore = MemoriaFactory.open(fStore.getFile());
  }
  
  protected final void reopen() {
    recreateStore(); 
  }

  protected final void save(Object...objects) {
    for(Object obj: objects) {
      fStore.save(obj);
    }
    fStore.writePendingChanges();
  }
  
  protected final void saveAll(Object obj) {
    fStore.saveAll(obj);
    fStore.writePendingChanges();
  }
  
  @Override
  protected void setUp() {
   // fFile = new File("fileStore.db");
   // fFile.delete(); 
    createStore();
  }

}
