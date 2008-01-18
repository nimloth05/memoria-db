package org.memoriadb.testutil;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.memoriadb.*;
import org.memoriadb.block.Block;
import org.memoriadb.core.IObjectInfo;
import org.memoriadb.core.block.IBlockManagerExt;
import org.memoriadb.core.file.*;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.mode.*;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.IObjectId;

public abstract class AbstractMemoriaTest extends TestCase {
  
  private static final String PATH = "file.mia";
  
  protected ObjectStore fObjectStore;
  protected DataStore fDataStore;
  
  public IBlockManagerExt getBlockManager() {
    return (IBlockManagerExt)fObjectStore.getBlockManager();
  }
  
  protected void assertBlocks(Block b1, Block b2) {
    assertEquals(b1.getPosition(), b2.getPosition());
    assertEquals(b1.getWholeSize(), b2.getWholeSize());
    assertEquals(b1.getInactiveRatio(), b2.getInactiveRatio());
  }
  
  protected final void assertTypeHierachy(Class<?> clazz) {
    IMemoriaClass memoriaClass = fObjectStore.getTypeInfo().getMemoriaClass(clazz);
    assertSame(memoriaClass, fObjectStore.getTypeInfo().getMemoriaClass(clazz.getName()));
    
    IMemoriaClass superClass = memoriaClass.getSuperClass();
    if (superClass == null) {
      assertEquals(Object.class.getName(), memoriaClass.getJavaClassName());
      return;
    }
    
    assertSame(superClass, fObjectStore.getTypeInfo().getMemoriaClass(clazz.getSuperclass()));
    assertSame(ReflectionUtil.getClass(superClass.getJavaClassName()), clazz.getSuperclass());

    assertTypeHierachy(clazz.getSuperclass());
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
  
  protected Block getBlock(int index) {
    return getBlockManager().getBlock(index);
  }
  
  protected IMemoriaFile getFile() {
    if(fObjectStore!=null) return (fObjectStore).getFile();
    return (fDataStore).getFile();
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
  
  protected <T> List<T> query(Class<T> clazz) {
    return fObjectStore.query(clazz);
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
  
  private ObjectStore openStore(IMemoriaFile file, CreateConfig config) {
    return (ObjectStore) Memoria.open(config, file);
  }
  
  private DataStore openStoreDataMode(IMemoriaFile file, CreateConfig config) {
    return (DataStore) Memoria.openInDataMode(config, file);
  }

  
}
