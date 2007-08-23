package org.memoriadb.test.core;

import java.io.File;
import java.util.*;

import junit.framework.TestCase;

import org.memoriadb.core.facade.*;
import org.memoriadb.core.file.*;

public abstract class AbstractObjectStoreTest extends TestCase {
  
  private static final String PATH = "file.mia";
  
  protected IMemoria fStore;
  
  
  protected final <T> List<T> getAll(Class<T> clazz) {
    List<T> result = new ArrayList<T>();
    for(Object object: fStore.getAllObjects()) {
      if (clazz.isInstance(object)) result.add(clazz.cast(object));
    }
    return result;
  }
  
  protected final void recreateStore() {
    fStore.close(); 
    //fStore = openFile(new PhysicalFile(PATH));
    InMemoryFile file = (InMemoryFile) fStore.getFile();
    file.reset();
    fStore = openFile(file);
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
   File file = new File(PATH);
   file.delete(); 
   //fStore = openFile(new PhysicalFile(PATH));
   fStore = openFile(new InMemoryFile());
  }

  @Override
  protected void tearDown() {
    fStore.close();
  }
  
  private IMemoria openFile(IMemoriaFile file) {
    return MemoriaFactory.open(file);
  }

}
