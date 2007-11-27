package org.memoriadb.core;

import java.io.IOException;
import java.util.*;

import org.memoriadb.block.*;
import org.memoriadb.core.block.SurvivorAgent;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.*;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.mode.*;
import org.memoriadb.core.util.*;
import org.memoriadb.id.*;
import org.memoriadb.instantiator.IInstantiator;

public class TransactionHandler {

  private final IObjectRepository fObjectRepository;

  private final ITransactionWriter fTransactionWriter;

  private final Set<ObjectInfo> fAdd = new IdentityHashSet<ObjectInfo>();
  private final Set<ObjectInfo> fUpdate = new IdentityHashSet<ObjectInfo>();
  private final Set<ObjectInfo> fDelete = new IdentityHashSet<ObjectInfo>();

  private int fUpdateCounter = 0;
  private final IInstantiator fInstantiator;
  private final Header fHeader;
  private final IModeStrategy fModeStrategy;

  public TransactionHandler(IInstantiator instantiator, ITransactionWriter writer, Header header,
      IModeStrategy modeStrategy) {
    fInstantiator = instantiator;
    fTransactionWriter = writer;
    fHeader = header;
    fModeStrategy = modeStrategy;
    fObjectRepository = writer.getRepo();
  }

  
  public IObjectId addMemoriaClass(Class<?> clazz) {
    Class<?> type = clazz;
    if (clazz.isArray()) {
      type = ReflectionUtil.getComponentTypeInfo(clazz).getJavaClass();
    }
    if (Type.isPrimitive(type)) throw new MemoriaException("primitive can not be added " + clazz);

    IObjectId result = ObjectModeStrategy.addMemoriaClassIfNecessary(this, clazz);
    
    if (!isInUpdateMode()) writePendingChanges();
    return result;
  }

  
  public void beginUpdate() {
    ++fUpdateCounter;
  }

  public void checkIndexConsistancy() {
    fObjectRepository.checkIndexConsistancy();
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

  public Collection<IObjectInfo> getAllObjectInfos() {
    return fObjectRepository.getAllObjectInfos();
  }

  public Collection<Object> getAllObjects() {
    return fObjectRepository.getAllObjects();
  }

  public IObjectId getArrayMemoriaClassId() {
    return fObjectRepository.getArrayMemoriaClass();
  }
  
  public IBlockManager getBlockManager() {
    return fTransactionWriter.getBlockManager();
  }
  
  public IDefaultIdProvider getDefaultIdProvider() {
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
    return fObjectRepository.getArrayMemoriaClass();
  }
  
  public IMemoriaClass getMemoriaClass(Object object) {
    IObjectId id = getMemoriaClassId(object);
    if(id == null) return null;
    return (IMemoriaClass) fObjectRepository.getObject(id); 
  }
  
  public IMemoriaClass getMemoriaClass(String className) {
    return fObjectRepository.getMemoriaClass(className);
  }
  
  public IObjectId getMemoriaClassId(Object object) {
    ObjectInfo info = getObjectInfo(object);
    if(info == null) return null;
    return info.getMemoriaClassId();
  }

  /**
   * @return The Class for the given <tt>obj</tt> or null.
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

  public Set<ObjectInfo> getSurvivors(Block block) {
    SurvivorAgent agent = new SurvivorAgent(fObjectRepository, fTransactionWriter.getFile());
    return agent.getSurvivors(block);
  }

  public TypeInfo getTypeInfo() {
    return new TypeInfo(this);
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
   * Saves the obj without considering if this ObjectStore is in update-mode or not.
   */
  public IObjectId internalSave(Object obj) {
    fModeStrategy.checkObject(obj);
    
    ObjectInfo info = getObjectInfo(obj);

    if (info != null) {
      if (fAdd.contains(info)) {
        // added in same transaction, don't add it again
        return fObjectRepository.getExistingId(obj);
      }

      // object already in the store, perform update. info is replaced if several updates occur in same transaction.
      fUpdate.add(info);
      return info.getId();
    }

    // object not already in the store, add it

    IObjectId memoriaClassId = addMemoriaClassIfNecessary(obj);
    fModeStrategy.checkCanInstantiateObject(this, memoriaClassId, fInstantiator);
    ObjectInfo result = fObjectRepository.add(obj, memoriaClassId);
    fAdd.add(getObjectInfo(obj));
    return result.getId();
  }

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
    return fModeStrategy.addMemoriaClassIfNecessary(this, obj);
  }

  private void internalDeleteAll(Object root) {
    if (!contains(root)) return;
    DeleteTraversal traversal = new DeleteTraversal(this);
    traversal.handle(root);
  }

  private IObjectId internalSaveAll(Object root) {
    SaveTraversal traversal = new SaveTraversal(this);
    traversal.handle(root);
    return fObjectRepository.getExistingId(root);
  }
}
