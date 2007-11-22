package org.memoriadb.core;

import java.io.IOException;
import java.util.*;

import org.memoriadb.core.block.*;
import org.memoriadb.core.file.*;
import org.memoriadb.core.id.*;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.mode.IModeStrategy;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.IdentityHashSet;

public class TransactionHandler implements ITransactionHandler {

  private final IObjectRepo fObjectRepo;
  
  private final ITransactionWriter fTransactionWriter;

  private final Set<ObjectInfo> fAdd = new IdentityHashSet<ObjectInfo>();
  private final Set<ObjectInfo> fUpdate = new IdentityHashSet<ObjectInfo>();
  private final Set<ObjectInfo> fDelete = new IdentityHashSet<ObjectInfo>();

  private int fUpdateCounter = 0;
  private final IDefaultInstantiator fDefaultInstantiator;
  private final FileHeader fHeader;
  private final IModeStrategy fStore;

  public TransactionHandler(IDefaultInstantiator defaultInstantiator, ITransactionWriter writer, FileHeader header, IModeStrategy store) {
    fDefaultInstantiator = defaultInstantiator;
    fTransactionWriter = writer;
    fHeader = header;
    fStore = store;
    fObjectRepo = writer.getRepo();
  }

  @Override
  public void beginUpdate() {
    ++fUpdateCounter;
  }

  @Override
  public void checkIndexConsistancy() {
    fObjectRepo.checkIndexConsistancy();
  }

  @Override
  public void close() {
    fTransactionWriter.close();
  }

  @Override
  public boolean contains(Object obj) {
    return fObjectRepo.contains(obj);
  }

  @Override
  public boolean containsId(IObjectId id) {
    return fObjectRepo.contains(id);
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

  public Collection<IObjectInfo> getAllObjectInfos() {
    return fObjectRepo.getAllObjectInfos();
  }

  @Override
  public Collection<Object> getAllObjects() {
    return fObjectRepo.getAllObjects();
  }

  @Override
  public IBlockManager getBlockManager() {
    return fTransactionWriter.getBlockManager();
  }

  public IMemoriaFile getFile() {
    return fTransactionWriter.getFile();
  }

  @Override
  public FileHeader getHeader() {
    return fHeader;
  }

  @Override
  public long getHeadRevision() {
    return fTransactionWriter.getHeadRevision();
  }

  @Override
  public IDefaultObjectIdProvider getIdFactory() {
    return fObjectRepo.getIdFactory();
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
    return getObject(getMemoriaClassId(obj));
  }

  @Override
  public IObjectId getMemoriaClassId(Object obj) {
    return getObjectInfo(obj).getMemoriaClassId();
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
  public ObjectInfo getObjectInfo(Object obj) {
    return fObjectRepo.getObjectInfo(obj);
  }

  @Override
  public IObjectInfo getObjectInfoForId(IObjectId id) {
    return fObjectRepo.getObjectInfoForId(id);
  }

  public IObjectRepo getObjectRepo() {
    return fObjectRepo;
  }

  @Override
  public Set<ObjectInfo> getSurvivors(Block block) {
    SurvivorAgent agent = new SurvivorAgent(fObjectRepo, fTransactionWriter.getFile());
    return agent.getSurvivors(block);
  }

  public void internalDelete(Object obj) {
    ObjectInfo info = getObjectInfo(obj);
    if (info == null) return;

    if (fAdd.remove(info)) {
      // object was added in current transaction, remove it from add-list and from the repo
      fObjectRepo.delete(obj);
      return;
    }

    // if object was previously updated in current transaction, remove it from update-list
    fUpdate.remove(info);

    fObjectRepo.delete(obj);
    fDelete.add(info);
  }

  public IMemoriaClassConfig internalGetMemoriaClass(String klass) {
    return fObjectRepo.getMemoriaClass(klass);
  }

  /**
   * Saves the obj without considering if this ObjectStore is in update-mode or not.
   */
  public IObjectId internalSave(Object obj) {
    ObjectInfo info = getObjectInfo(obj);

    if (info != null) {
      if (fAdd.contains(info)) {
        // added in same transaction, don't add it again
        return fObjectRepo.getObjectId(obj);
      }

      // object already in the store, perform update. info is replaced if several updates occur in same transaction.
      fUpdate.add(info);
      return info.getId();
    }

    // object not already in the store, add it

    IObjectId memoriaClassId = addMemoriaClassIfNecessary(obj);
    fStore.checkCanInstantiateObject(this, memoriaClassId, fDefaultInstantiator);
    ObjectInfo result = fObjectRepo.add(obj, memoriaClassId);
    fAdd.add(getObjectInfo(obj));
    return result.getId();
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
 
  public IObjectId saveAll(Object root) {
    IObjectId result = internalSaveAll(root);
    if (!isInUpdateMode()) writePendingChanges();
    return result;
  }

  public void writePendingChanges() {
    if (fAdd.isEmpty() && fUpdate.isEmpty() && fDelete.isEmpty()) return;

    try {
      fTransactionWriter.write(fAdd, fUpdate, fDelete);
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }

    fAdd.clear();
    fUpdate.clear();
    fDelete.clear();
  }

  private IObjectId addMemoriaClassIfNecessary(Object obj) {
    return fStore.addMemoriaClassIfNecessary(this, obj);
  }

  private void internalDeleteAll(Object root) {
    if(!contains(root)) return;
    DeleteTraversal traversal = new DeleteTraversal(this);
    traversal.handle(root);
  }

  private IObjectId internalSaveAll(Object root) {
    SaveTraversal traversal = new SaveTraversal(this);
    traversal.handle(root);
    return fObjectRepo.getObjectId(root);
  }
}
