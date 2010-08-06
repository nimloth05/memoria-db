/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.testutil;

import junit.framework.TestCase;
import org.memoriadb.CreateConfig;
import org.memoriadb.Memoria;
import org.memoriadb.TestMode;
import org.memoriadb.block.Block;
import org.memoriadb.core.IObjectInfo;
import org.memoriadb.core.block.IBlockManagerExt;
import org.memoriadb.core.file.*;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.mode.DataStore;
import org.memoriadb.core.mode.ObjectStore;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.IObjectId;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class AbstractMemoriaTest extends TestCase {
  
  private static final File PATH = new File("file.mia");
  
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
    return fObjectStore.<T>get(id);
  }
  
  protected Block getBlock(int index) {
    return getBlockManager().getBlock(index);
  }
  
  protected Block getBlockForObjectId(IObjectId id) {
    IObjectInfo info = getObjectInfo(id);
    return fObjectStore.getBlockRepository().getBlock(info);
  }

  protected IMemoriaFile getFile() {
    IMemoriaFile file = getCurrentFile();
    if (file instanceof IMemoriaFileDecorator) {
      file = ((IMemoriaFileDecorator)file).getFile();
    }
    return file; 
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
      fDataStore = openStoreDataMode(createPhysicalFile(), config);
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
      fObjectStore = openStore(createPhysicalFile(), config);
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
     PATH.delete(); 
     fObjectStore = openStore(createPhysicalFile(), config);
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
  
  private PhysicalFile createPhysicalFile() {
    try {
      return new PhysicalFile(PATH);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  private IMemoriaFile getCurrentFile() {
    if(fObjectStore != null) return (fObjectStore).getFile();
    return (fDataStore).getFile();
  }
  
  private ObjectStore openStore(IMemoriaFile file, CreateConfig config) {
    return (ObjectStore) Memoria.open(config, file);
  }
  
  private DataStore openStoreDataMode(IMemoriaFile file, CreateConfig config) {
    return (DataStore) Memoria.openInDataMode(config, file);
  }

  protected long getRevision(IObjectId id) {
    return getBlockForObjectId(id).getRevision();
  }

  protected long getRevision(IObjectInfo info) {
    return getRevision(info.getId());
  }
  
}
