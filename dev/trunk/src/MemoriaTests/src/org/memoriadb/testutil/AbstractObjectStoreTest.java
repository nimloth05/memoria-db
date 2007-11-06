package org.memoriadb.testutil;

import java.io.File;
import java.util.*;

import junit.framework.TestCase;

import org.memoriadb.*;
import org.memoriadb.core.ObjectStore;
import org.memoriadb.core.file.*;
import org.memoriadb.core.id.IObjectId;

public abstract class AbstractObjectStoreTest extends TestCase {
  
  private static final String PATH = "file.mia";
  
  protected IObjectStore fStore;
  
  protected void beginUpdate() {
    fStore.beginUpdate();
  }
  
  protected void delete(Object obj) {
    fStore.delete(obj);
  }
  
  protected void deleteAll(Object obj) {
    fStore.deleteAll(obj);
  }

  protected void endUpdate() {
    fStore.endUpdate();
  }
  
  protected final <T> List<T> getAll(Class<T> clazz) {
    List<T> result = new ArrayList<T>();
    for(Object object: fStore.getAllObjects()) {
      if (clazz.isInstance(object)) result.add(clazz.cast(object));
    }
    return result;
  }
  
  protected IMemoriaFile getFile() {
    return ((ObjectStore)fStore).getFile();
  }
  
  protected final void recreateStore() {
    fStore.close(); 
    
    //fStore = openFile(new PhysicalFile(PATH));
    
    InMemoryFile file = (InMemoryFile) getFile();
    file.reset();
    fStore = openFile(file);
  }
  
  protected final void reopen() {
    recreateStore(); 
  }

  protected final IObjectId save(Object obj) {
    return fStore.save(obj);
  }
  
  protected final IObjectId[] save(Object...objects) {
    return fStore.save(objects);
  }
  
  protected final IObjectId saveAll(Object obj) {
    return fStore.saveAll(obj);
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
