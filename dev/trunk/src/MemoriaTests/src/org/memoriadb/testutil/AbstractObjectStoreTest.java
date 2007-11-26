package org.memoriadb.testutil;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.memoriadb.*;
import org.memoriadb.core.*;
import org.memoriadb.core.block.IBlockManagerExt;
import org.memoriadb.core.file.*;
import org.memoriadb.core.handler.IDataObject;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.mode.*;

public abstract class AbstractObjectStoreTest extends TestCase {
  
  private static final String PATH = "file.mia";
  
  protected IObjectStoreExt fObjectStore;
  protected IDataStoreExt fDataStore;
  
  public IBlockManagerExt getBlockManager() {
    return (IBlockManagerExt)fObjectStore.getBlockManager();
  }
  
  protected void beginUpdate() {
    fObjectStore.beginUpdate();
  }
  
  /**
   * Overwrite to change the memoria-configuration for opening a db. 
   */
  protected void configureOpen(CreateConfig config) {}

  /**
   * Overwrite to change the memoria-configuration for reopening a db. 
   */
  protected void configureReopen(CreateConfig config) {}
  
  protected void delete(Object obj) {
    fObjectStore.delete(obj);
  }
  
  protected void deleteAll(Object obj) {
    fObjectStore.deleteAll(obj);
  }
  
  protected void endUpdate() {
    fObjectStore.endUpdate();
  }
  
  protected <T> T get(IObjectId id) {
    return fObjectStore.get(id);
  }

  protected <T> List<T> query(Class<T> clazz) {
    return fObjectStore.query(clazz);
  }
  
  protected IMemoriaFile getFile() {
    if(fObjectStore!=null) return ((ObjectStore)fObjectStore).getFile();
    return ((DataStore)fDataStore).getFile();
  }
  
  protected LastWrittenBlockInfo getLastBlockInfo() {
    return fObjectStore.getHeader().getLastWrittenBlockInfo();
  }

  protected IObjectInfo getObjectInfo(IObjectId id) {
    return fObjectStore.getObjectInfoForId(id);
  }
  
  protected int getOPF() {
    return FileLayout.OPF;
  }
  
  protected int getOPO() {
    return FileLayout.getOPO(fObjectStore);
  }
  
  protected TestMode getTestMode() {
    return TestMode.memory;
  }

  protected final void recreateDataStore() {
    closeStores();
    
    CreateConfig config = new CreateConfig();
    configureReopen(config);
    
    if (getTestMode() == TestMode.memory) {
      InMemoryFile file = (InMemoryFile) getFile();
      file.reset();
      fDataStore = openStoreDataMode(file, config);
    } else {
      fDataStore = openStoreDataMode(new PhysicalFile(PATH), config);
    }
    fObjectStore = null;
  }
  
  protected final void recreateObjectStore() {
    closeStores();
    
    CreateConfig config = new CreateConfig();
    configureReopen(config);
    
    if (getTestMode() == TestMode.memory) {
      InMemoryFile file = (InMemoryFile) getFile();
      file.reset();
      fObjectStore = openStore(file, config);
    } else {
      fObjectStore = openStore(new PhysicalFile(PATH), config);
    }
    fDataStore = null;
  }

  protected final void reopen() {
    recreateObjectStore(); 
  }
  
  protected void reopenDataMode() {
    recreateDataStore();
  }
   
  protected final IObjectId save(IDataObject obj) {
    return fDataStore.save(obj);
  }

  protected final IObjectId save(Object obj) {
    return fObjectStore.save(obj);
  }
  
  protected final IObjectId saveAll(IDataObject obj) {
    return fDataStore.saveAll(obj);
  }
  
  protected final IObjectId saveAll(Object obj) {
    return fObjectStore.saveAll(obj);
  }
  
  @Override
  protected void setUp() {
   CreateConfig config = new CreateConfig();
   configureOpen(config);
   
   if (getTestMode() == TestMode.memory) {
     fObjectStore = openStore(new InMemoryFile(), config);
   } 
   else {
     File file = new File(PATH);
     file.delete(); 
     fObjectStore = openStore(new PhysicalFile(PATH), config);
   }
  }

  @Override
  protected void tearDown() {
    closeStores();
  }
  
  private void closeStores() {
    if(fObjectStore != null) fObjectStore.close();
    if(fDataStore != null) fDataStore.close();
  }
  
  private IObjectStoreExt openStore(IMemoriaFile file, CreateConfig config) {
    return (IObjectStoreExt) Memoria.open(config, file);
  }
  
  private IDataStoreExt openStoreDataMode(IMemoriaFile file, CreateConfig config) {
    return (IDataStoreExt) Memoria.openDataMode(config, file);
  }
  
}
