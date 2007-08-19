package org.memoriadb.test.core;

import java.io.File;
import java.util.*;

import junit.framework.TestCase;

import org.memoriadb.core.facade.*;

public abstract class AbstractObjectStoreTest extends TestCase {
  
  protected IMemoria fStore;
  
  private File fFile;
  
  protected final void createStore() {
    fStore = MemoriaFactory.open(fFile);
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
    fFile = new File("fileStore.db");
    fFile.delete(); 
    createStore();
  }

}
