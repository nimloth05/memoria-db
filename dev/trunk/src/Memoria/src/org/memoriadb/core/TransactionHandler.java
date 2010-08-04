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

package org.memoriadb.core;

import java.util.*;

import org.memoriadb.block.*;
import org.memoriadb.core.block.*;
import org.memoriadb.core.exception.*;
import org.memoriadb.core.file.*;
import org.memoriadb.core.file.write.TransactionWriter;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.mode.IModeStrategy;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.core.util.collection.identity.IdentityHashSet;
import org.memoriadb.id.*;

public final class TransactionHandler {

  private final IObjectRepository fObjectRepository;
  private final TransactionWriter fTransactionWriter;

  // use IdentityHashSets for better performance
  private final Set<ObjectInfo> fAdd = IdentityHashSet.create();
  private final Set<ObjectInfo> fUpdate = IdentityHashSet.create();
  private final Set<ObjectInfo> fDelete = IdentityHashSet.create();

  private int fUpdateCounter = 0;
  private final Header fHeader;
  private final IModeStrategy fModeStrategy;

  public TransactionHandler(TransactionWriter writer, Header header, IModeStrategy modeStrategy) {
    fTransactionWriter = writer;
    fHeader = header;
    fModeStrategy = modeStrategy;
    fObjectRepository = writer.getRepo();
  }

  public IObjectId addMemoriaClassIfNecessary(final Class<?> clazz) {
    Class<?> type = clazz;
    
    if (clazz.isArray()) {
      type = ReflectionUtil.getComponentTypeInfo(clazz).getJavaClass();
    }
    if (Type.isPrimitive(type)) throw new MemoriaException("primitive can not be added " + clazz);

    IObjectId result = TypeHierarchyBuilder.addMemoriaClassIfNecessary(this, clazz, fModeStrategy);
    
    if (!isInUpdateMode()) writePendingChanges();
    return result;
  }

  public IObjectId addMemoriaClassIfNecessary(Object obj) {
    return fModeStrategy.addMemoriaClassIfNecessary(this, obj);
  }

  public void beginUpdate() {
    ++fUpdateCounter;
  }

  public void checkIndexConsistency() {
    fObjectRepository.checkIndexConsistency();
  }
  
  public void close() {
    fTransactionWriter.close();
  }
  
  public boolean contains(Object obj) {
    return fObjectRepository.contains(obj);
  }

  public boolean containsId(IObjectId id) {
    return fObjectRepository.contains(id);
  }
  
  public void delete(Object obj) {
    internalDelete(obj);
    if (!isInUpdateMode()) writePendingChanges();
  }

  public void deleteAll(Object root) {
    internalDeleteAll(root);
    if (!isInUpdateMode()) writePendingChanges();
  }

  public void endUpdate() {
    if (fUpdateCounter == 0) throw new MemoriaException("ObjectStore is not in update-mode");
    --fUpdateCounter;
    if (fUpdateCounter != 0) return;

    writePendingChanges();
  }

  public Iterable<IMemoriaClass> getAllClasses() {
    return fObjectRepository.getAllClasses();
  }
  
  public Collection<IObjectInfo> getAllObjectInfos() {
    return fObjectRepository.getAllObjectInfo();
  }
  
  public Iterable<Object> getAllObjects() {
    return fObjectRepository.getAllObjects();
  }
  
  public Iterable<Object> getAllUserSpaceObjects() {
    return fObjectRepository.getAllUserSpaceObjects();
  }

  public IObjectId getArrayMemoriaClassId() {
    return fObjectRepository.getIdFactory().getArrayMemoriaClass();
  }
  
  public IBlockManager getBlockManager() {
    return fTransactionWriter.getBlockManager();
  }

  public BlockRepository getBlockRepository() {
    return fTransactionWriter.getBlockRepository();
  }
  
  public IIdProvider getDefaultIdProvider() {
    return fObjectRepository.getIdFactory();
  }
  
  public IObjectId getExistingId(Object obj) {
    return fObjectRepository.getExistingId(obj);
  }
  
  public IMemoriaFile getFile() {
    return fTransactionWriter.getFile();
  }
  
  public Header getHeader() {
    return fHeader;
  }
  
  public long getHeadRevision() {
    return fTransactionWriter.getHeadRevision();
  }
  
  public IObjectId getId(Object obj) {
    return fObjectRepository.getId(obj);
  }

  public int getIdSize() {
    return fObjectRepository.getIdFactory().getIdSize();
  }

  public IObjectId getMemoriaArrayClass() {
    return fObjectRepository.getIdFactory().getArrayMemoriaClass();
  }

  public IMemoriaClass getMemoriaClass(Object object) {
    return fModeStrategy.getMemoriaClass(object, fObjectRepository);
  }
  
  public IMemoriaClass getMemoriaClass(String className) {
    return fObjectRepository.getMemoriaClass(className);
  }

  public IObjectId getMemoriaClassId(Object object) {
    IObjectInfo info = getObjectInfo(object);
    if(info == null) return null;
    return info.getMemoriaClassId();
  }

  /**
   * @param className
   * @return The Class for the given <tt>obj</tt> or null.
   * @param className
   */
  public IObjectId getMemoriaClassId(String className) {
    return fObjectRepository.getId(getMemoriaClass(className));
  }

  @SuppressWarnings("unchecked")
  public <T> T getObject(IObjectId id) {
    return (T) fObjectRepository.getObject(id);
  }

  public ObjectInfo getObjectInfo(Object obj) {
    return fObjectRepository.getObjectInfo(obj);
  }

  public IObjectInfo getObjectInfoForId(IObjectId id) {
    return fObjectRepository.getObjectInfoForId(id);
  }

  public IObjectRepository getObjectRepo() {
    return fObjectRepository;
  }

  public SurvivorAgent getSurvivorAgent(Block block) {
    return new SurvivorAgent(fObjectRepository, getBlockRepository(), block);
  }

  //FIXME: TypeInfo bereinigen
  public TypeInfo getTypeInfo() {
    return new TypeInfo(this);
  }

  public IObjectId internalAddObject(Object obj, IObjectId memoriaClassId) {
    if (contains(obj)) throw new MemoriaException("obj already added: " + obj);
    if(((IMemoriaClass)getObject(memoriaClassId)).isValueObject()) throw new MemoriaException("A ValueObject can not be added. Type: " + obj.getClass().getName() + " .toString() " + obj);    

    fModeStrategy.checkCanInstantiateObject(this, memoriaClassId, fHeader.getInstantiator());

    ObjectInfo result = fObjectRepository.add(obj, memoriaClassId);
    fAdd.add(result);
    return result.getId();
  }

  public void internalDelete(Object obj) {
    ObjectInfo info = getObjectInfo(obj);
    if (info == null) return;

    if (fAdd.remove(info)) {
      // object was added in current transaction, remove it from add-list and from the repo
      fObjectRepository.delete(obj);
      return;
    }

    // if object was previously updated in current transaction, remove it from update-list
    fUpdate.remove(info);

    fObjectRepository.delete(obj);
    fDelete.add(info);
  }
  
  public IMemoriaClassConfig internalGetMemoriaClass(String klass) {
    return fObjectRepository.getMemoriaClass(klass);
  }

  /**
   * Saves the given memoriaClass as Object.
   * 
   * @param clazz memoriaClass to save
   * @return id of the class.
   */
  public IObjectId internalSave(IMemoriaClass clazz) {
    return internalSave(clazz, clazz.getMemoriaClassId());
  }

  /**
   * Saves the obj without considering if this ObjectStore is in update-mode or not.
   * @param obj
   * @param obj
   * @param memoriaClassId
   * @param memoriaClassId
   * @return
   */
  public IObjectId internalSave(Object obj, IObjectId memoriaClassId) {
    fModeStrategy.checkObject(obj);
    
    ObjectInfo info = getObjectInfo(obj);

    if (info != null) return internalUpdateObject(obj, info);
    return internalAddObject(obj, memoriaClassId);
  }

  public boolean isEnum(Object obj) {
    return fModeStrategy.isEnum(obj);
  }

  public boolean isInUpdateMode() {
    return fUpdateCounter > 0;
  }

  public IObjectId save(Object obj) {
    if (isEnum(obj)) throw new SchemaException("It is not possible to add enum-instances. They are automatically saved when referenced.");

    SaveTraversal traversal = new SaveTraversal(this);
    traversal.handle(obj);

    if (!isInUpdateMode()) writePendingChanges();
    return fObjectRepository.getExistingId(obj);
  }

  public IObjectId saveAll(Object root) {
    SaveAllTraversal traversal = new SaveAllTraversal(this);
    traversal.handle(root);
    
    if (!isInUpdateMode()) writePendingChanges();
    return fObjectRepository.getExistingId(root);
  }

  public void writePendingChanges() {
    if (fAdd.isEmpty() && fUpdate.isEmpty() && fDelete.isEmpty()) return;

    try {
      fTransactionWriter.write(fAdd, fUpdate, fDelete, fModeStrategy);
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }

    fAdd.clear();
    fUpdate.clear();
    fDelete.clear();
  }

  private void internalDeleteAll(Object root) {
    if (!contains(root)) return;
    DeleteTraversal traversal = new DeleteTraversal(this);
    traversal.handle(root);
  }

  private IObjectId internalUpdateObject(Object obj, ObjectInfo info) {
    if (fAdd.contains(info)) {
      // added in same transaction, don't update it
      return fObjectRepository.getExistingId(obj);
    }

    // object already in the store, perform update. info is replaced if several updates occur in same transaction.
    fUpdate.add(info);
    return info.getId();
  }
  
}
