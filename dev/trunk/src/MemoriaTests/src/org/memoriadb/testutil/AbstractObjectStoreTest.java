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
  
  public IBlockManagerExt getBlockManager() {
    return (IBlockManagerExt)fStore.getBlockManager();
  }
  
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
  
  protected Object get(IObjectId id) {
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
  
  protected DBMode getInitialDBMode() {
    return DBMode.clazz;
  }

  protected IObjectInfo getObjectInfo(IObjectId id) {
    return fStore.getObjectInfo(id);
  }
  
  protected int getOPF() {
    return FileLayout.OPF;
  }
  
  protected int getOPO() {
    return FileLayout.getOPO(fStore);
  }
  
  protected final void recreateStore(DBMode mode) {
    fStore.close(); 
    
    //fStore = openFile(new PhysicalFile(PATH));
    
    InMemoryFile file = (InMemoryFile) getFile();
    file.reset();
    fStore = openFile(file, mode);
  }

  protected final void reopen() {
    recreateStore(DBMode.clazz); 
  }
  
  protected final void reopen(DBMode mode) {
    recreateStore(mode); 
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
   fStore = openFile(new InMemoryFile(), getInitialDBMode());
  }
  
  @Override
  protected void tearDown() {
    fStore.close();
  }
   
  private IObjectStoreExt openFile(IMemoriaFile file, DBMode mode) {
    CreateConfig config = new CreateConfig();
    config.setDBMode(mode);
    return (IObjectStoreExt) Memoria.open(config, file);
  }
}
