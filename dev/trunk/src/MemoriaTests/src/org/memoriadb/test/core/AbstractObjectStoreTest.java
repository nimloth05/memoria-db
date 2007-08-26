package org.memoriadb.test.core;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.memoriadb.*;
import org.memoriadb.core.ObjectStore;
import org.memoriadb.core.file.*;

public abstract class AbstractObjectStoreTest extends TestCase {
  
  private static final String PATH = "file.mia";
  
  protected IObjectStore fStore;
  
  
  protected final <T> List<T> getAll(Class<T> clazz) {
    return fStore.getAll(clazz);
  }
  
  protected final <T> List<T> getAll(Class<T> clazz, IFilter<T> filter) {
    return fStore.getAll(clazz, filter);
  }
  
  protected final void recreateStore() {
    fStore.close(); 
    //fStore = openFile(new PhysicalFile(PATH));
    InMemoryFile file = (InMemoryFile) ((ObjectStore)fStore).getFile();
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
  }
  
  protected final void saveAll(Object obj) {
    fStore.saveAll(obj);
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
  
  private IObjectStore openFile(IMemoriaFile file) {
    return Memoria.open(file);
  }

}
