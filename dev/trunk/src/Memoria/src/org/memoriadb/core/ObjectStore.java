package org.memoriadb.core;

import java.io.IOException;
import java.util.*;

import org.memoriadb.IFilter;
import org.memoriadb.core.block.*;
import org.memoriadb.core.file.*;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.IdentityHashSet;

public class ObjectStore implements IObjectStoreExt {

  private final IObjectRepo fObjectRepo;
  private final ITransactionWriter fTransactionWriter;
  private final DBMode fDBMode;

  // FIXME Sets of ObjectInfos could increase the performance when updating the current block
  private final Set<Object> fAdd = new IdentityHashSet<Object>();
  private final Set<Object> fUpdate = new IdentityHashSet<Object>();
  private final Set<IObjectId> fDelete = new IdentityHashSet<IObjectId>();

  private int fUpdateCounter = 0;

  public ObjectStore(DBMode dbMode, IObjectRepo objectContainer, IMemoriaFile file, IBlockManager blockManager, long headRevision) {
    fObjectRepo = objectContainer;
    fTransactionWriter = new TransactionWriter(file, blockManager, headRevision);
    fDBMode = dbMode;
  }

  @Override
  public void beginUpdate() {
    ++fUpdateCounter;
  }

  @Override
  public void checkSanity() {
    fObjectRepo.checkSanity();
  }

  @Override
  public void close() {
    fTransactionWriter.close();
  }

  @Override
  public boolean contains(IObjectId id) {
    return fObjectRepo.contains(id);
  }

  @Override
  public boolean contains(Object obj) {
    return fObjectRepo.contains(obj);
  }

  @Override
  public void delete(IObjectId objectId) {
    delete(fObjectRepo.getObject(objectId));
  }

  @Override
  public void delete(Object obj) {
    internalDelete(obj);
    if (!isInUpdateMode()) writePendingChanges();
  }

  @Override
  public void deleteAll(Object root) {
    internalDeleteAll(root);
    if (!isInUpdateMode()) writePendingChanges();
  }

  @Override
  public void endUpdate() {
    if (fUpdateCounter == 0) throw new MemoriaException("ObjectStore is not in update-mode");
    --fUpdateCounter;
    if (fUpdateCounter != 0) return;

    writePendingChanges();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> List<T> getAll(Class<T> clazz) {
    return (List<T>) getAll(clazz.getName());
  }

  @Override
  public <T> List<T> getAll(Class<T> clazz, IFilter<T> filter) {
    List<T> result = getAll(clazz);
    Iterator<T> iterator = result.iterator();
    while(iterator.hasNext()) {
      if (!filter.accept(iterator.next())) iterator.remove();
    }
    return result;
  }
  
  @Override
  public List<Object> getAll(String clazz) {
    List<Object> result = new ArrayList<Object>();
    for (IObjectInfo objectInfo : getAllObjectInfos()) {
      IMemoriaClass memoriaClass = (IMemoriaClass) fObjectRepo.getObject(objectInfo.getMemoriaClassId());
      if (memoriaClass.isTypeFor(clazz)) result.add(objectInfo.getObj());
    }
    return result;
  }

  @Override
  public List<Object> getAll(String clazz, IFilter<Object> filter) {
    List<Object> result = new ArrayList<Object>();
    
    for (IObjectInfo objectInfo : getAllObjectInfos()) {
      IMemoriaClass memoriaClass = (IMemoriaClass) fObjectRepo.getObject(objectInfo.getMemoriaClassId());
      
      if (memoriaClass.isTypeFor(clazz)) {
        if (filter.accept(objectInfo.getObj())) result.add(objectInfo.getObj());
      }
    }
    return result;
  }

  public Collection<IObjectInfo> getAllObjectInfos() {
    return fObjectRepo.getAllObjectInfos();
  }
  
  @Override
  public Collection<Object> getAllObjects() {
    return fObjectRepo.getAllObjects();
  }

  @Override
  public IObjectId getArrayMetaClass() {
    return fObjectRepo.getArrayMemoriaClass();
  }

  @Override
  public IBlockManager getBlockManager() {
    return fTransactionWriter.getBlockManager();
  }

  public IMemoriaFile getFile() {
    return fTransactionWriter.getFile();
  }

  @Override
  public IObjectId getHandlerMetaClass() {
    return fObjectRepo.getHandlerMetaClass();
  }
  
  @Override
  public long getHeadRevision() {
    return fTransactionWriter.getHeadRevision();
  }

  @Override
  public int getIdSize() {
    return fObjectRepo.getIdFactory().getIdSize();
  }

  @Override
  public IMemoriaClass getMemoriaClass(Class<?> clazz) {
    return fObjectRepo.getMemoriaClass(clazz.getName());
  }

  @Override
  public IMemoriaClass getMemoriaClass(Object obj) {
    return getMemoriaClass(obj.getClass());
  }

  @Override
  public IObjectId getMemoriaFieldMetaClass() {
    return fObjectRepo.getMemoriaMetaClass();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getObject(IObjectId id) {
    return (T) fObjectRepo.getObject(id);
  }

  @Override
  public IObjectId getObjectId(Object obj) {
    return fObjectRepo.getObjectId(obj);
  }

  @Override
  public IObjectInfo getObjectInfo(IObjectId id) {
    return fObjectRepo.getObjectInfo(id); 
  }

  @Override
  public IObjectInfo getObjectInfo(Object obj) {
    return fObjectRepo.getObjectInfo(obj);
  }

  @Override
  public boolean isInUpdateMode() {
    return fUpdateCounter > 0;
  }
  
  public IObjectId save(Object obj) {
    IObjectId result = internalSave(obj);
    if (!isInUpdateMode()) writePendingChanges();
    return result;
  }

  @Override
  public IObjectId[] save(Object... objs) {
    IObjectId[] result = new IObjectId[objs.length];

    for (int i = 0; i < objs.length; ++i) {
      result[i] = internalSave(objs[i]);
    }

    if (!isInUpdateMode()) writePendingChanges();
    return result;
  }

  public IObjectId saveAll(Object root) {
    IObjectId result = internalSaveAll(root);
    if (!isInUpdateMode()) writePendingChanges();
    return result;
  }

  public IObjectId[] saveAll(Object... roots) {
    IObjectId[] result = new IObjectId[roots.length];

    for (int i = 0; i < roots.length; ++i) {
      result[i] = internalSaveAll(roots[i]);
    }

    if (!isInUpdateMode()) writePendingChanges();
    return result;
  }

  public void writePendingChanges() {
    if(fAdd.isEmpty() && fUpdate.isEmpty() && fDelete.isEmpty()) return;
    
    ObjectSerializer serializer = new ObjectSerializer(fObjectRepo);
    for(Object obj: fAdd) {
      serializer.serialize(obj);
    }
    for(Object obj: fUpdate) {
      fObjectRepo.updateObjectInfoUpdated(obj);
      serializer.serialize(obj);
    }
    for(IObjectId id: fDelete){
      fObjectRepo.updateObjectInfoDeleted(id);
      serializer.markAsDeleted(id);
    }
    
    try {
      Block block = fTransactionWriter.write(serializer.getBytes(), getPendingObjectCount());
      updateCurrentBlock(fAdd, block);
      updateCurrentBlock(fUpdate, block);
      updateCurrentBlockForDeleted(fDelete, block);
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
    
    
    fAdd.clear();
    fUpdate.clear();
    fDelete.clear();
  }

  void internalDelete(Object obj) {
    if(!fObjectRepo.contains(obj)) return;
    
    if(fAdd.remove(obj)){
      // object was added in current transaction, remove it from add-list and from the repo
      fObjectRepo.delete(obj);
      return;
    }
    
    // if object was previously updated in current transaction, remove it from update-list
    fUpdate.remove(obj);
    
    IObjectId id = fObjectRepo.delete(obj);
    fDelete.add(id);
  }

  IMemoriaClassConfig internalGetMemoriaClass(String klass) {
    return fObjectRepo.getMemoriaClass(klass);
  }
  
  /**
   * Saves the obj without considering if this ObjectStore is in update-mode or not.
   */
  /*package*/ IObjectId internalSave(Object obj) {
    
    // if the object was previous removed in the current transaction, the remove-marker will not be set
    // and the object is updated.
    if(fDelete.remove(obj)) {
      fUpdate.add(obj);
      return fObjectRepo.getObjectId(obj); 
    }
    
    if (fAdd.contains(obj)) {
      // added in same transaction
      return fObjectRepo.getObjectId(obj);
    }

    if (fObjectRepo.contains(obj)) {
      // object already in the store, perform update. obj is replaced if several updates occur in same transaction.
      fUpdate.add(obj);
      return fObjectRepo.getObjectId(obj);
    }

    // object not already in the store, add it
    fAdd.add(obj);
    IObjectId memoriaClassId = addMemoriaClassIfNecessary(obj);
    return fObjectRepo.add(obj, memoriaClassId);
  }

  private IObjectId addMemoriaClassIfNecessary(Object obj) {
    return fDBMode.addMemoriaClassIfNecessary(obj, this);
  }

  private int getPendingObjectCount() {
    return fAdd.size() + fUpdate.size() + fDelete.size();
  }

  private void internalDeleteAll(Object root) {
    DeleteTraversal traversal = new DeleteTraversal(this);
    traversal.handle(root);
  }

  private IObjectId internalSaveAll(Object root) {
    SaveTraversal traversal = new SaveTraversal(this);
    traversal.handle(root);
    return fObjectRepo.getObjectId(root);
  }

  private void updateCurrentBlock(Set<Object> objs, Block block) {
    for(Object obj: objs){
      getObjectInfo(obj).changeCurrentBlock(block);
    } 
  }

  private void updateCurrentBlockForDeleted(Set<IObjectId> ids, Block block) {
    for(IObjectId id: ids){
      getObjectInfo(id).changeCurrentBlock(block);
    } 
  }

}
