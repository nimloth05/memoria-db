package org.memoriadb.testutil;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.memoriadb.*;
import org.memoriadb.core.*;
import org.memoriadb.core.block.IBlockManagerExt;
import org.memoriadb.core.file.*;
import org.memoriadb.core.id.IObjectId;

public abstract class AbstractObjectStoreTest extends TestCase {
  
  private static final String PATH = "file.mia";
  
  protected IObjectStoreExt fStore;
  private DBMode fReopenDbMode = DBMode.clazz;
  
  public IBlockManagerExt getBlockManager() {
    return (IBlockManagerExt)fStore.getBlockManager();
  }
  
  protected void beginUpdate() {
    fStore.beginUpdate();
  }
  
  /**
   * Overwrite to change the memoria-configuration for opening a db. 
   */
  protected void configureOpen(CreateConfig config) {}

  /**
   * Overwrite to change the memoria-configuration for reopening a db. 
   */
  protected void configureReopen(CreateConfig config) {
    config.setDBMode(fReopenDbMode);
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
  
  protected <T> T get(IObjectId id) {
    return fStore.getObject(id);
  }
  
  protected final <T> List<T> getAll(Class<T> clazz) {
    return fStore.getAll(clazz);
  }
  
  protected final <T> List<T> getAll(Class<T> clazz, IFilter<T> filter) {
    return fStore.getAll(clazz, filter);
  }

  protected IMemoriaFile getFile() {
    return ((ObjectStore)fStore).getFile();
  }
  
  protected LastWrittenBlockInfo getLastBlockInfo() {
    return fStore.getHeader().getLastWrittenBlockInfo();
  }
  
  protected IObjectInfo getObjectInfo(IObjectId id) {
    return fStore.getObjectInfoForId(id);
  }

  protected int getOPF() {
    return FileLayout.OPF;
  }
  
  protected int getOPO() {
    return FileLayout.getOPO(fStore);
  }
  
  protected TestMode getTestMode() {
    return TestMode.memory;
  }
  
  protected final void recreateStore() {
    fStore.close(); 
    
    CreateConfig config = new CreateConfig();
    configureReopen(config);
    
    if (getTestMode() == TestMode.memory) {
      InMemoryFile file = (InMemoryFile) getFile();
      file.reset();
      fStore = openFile(file, config);
    } else {
      fStore = openFile(new PhysicalFile(PATH), config);
    }
  }
  
  
  
  protected final void reopen() {
    recreateStore(); 
  }
  
  protected void reopen(DBMode data) {
    fReopenDbMode = data;
    reopen();
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
   
   CreateConfig config = new CreateConfig();
   configureOpen(config);
   
   if (getTestMode() == TestMode.memory) {
     fStore = openFile(new InMemoryFile(), config);
   } else {
     fStore = openFile(new PhysicalFile(PATH), config);
   }
  }
  
  @Override
  protected void tearDown() {
    fStore.close();
  }

  private IObjectStoreExt openFile(IMemoriaFile file, CreateConfig config) {
    return (IObjectStoreExt) Memoria.open(config, file);
  }
  
}
