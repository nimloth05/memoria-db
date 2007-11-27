package org.memoriadb.core;

import java.io.IOException;
import java.util.*;

import org.memoriadb.core.block.*;
import org.memoriadb.core.file.*;
import org.memoriadb.core.id.*;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.mode.*;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.*;

public class TransactionHandler implements ITransactionHandler {

  private final IObjectRepository fObjectRepository;

  private final ITransactionWriter fTransactionWriter;

  private final Set<ObjectInfo> fAdd = new IdentityHashSet<ObjectInfo>();
  private final Set<ObjectInfo> fUpdate = new IdentityHashSet<ObjectInfo>();
  private final Set<ObjectInfo> fDelete = new IdentityHashSet<ObjectInfo>();

  private int fUpdateCounter = 0;
  private final IDefaultInstantiator fDefaultInstantiator;
  private final Header fHeader;
  private final IModeStrategy fModeStrategy;

  public TransactionHandler(IDefaultInstantiator defaultInstantiator, ITransactionWriter writer, Header header,
      IModeStrategy modeStrategy) {
    fDefaultInstantiator = defaultInstantiator;
    fTransactionWriter = writer;
    fHeader = header;
    fModeStrategy = modeStrategy;
    fObjectRepository = writer.getRepo();
  }

  @Override
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

  @Override
  public void beginUpdate() {
    ++fUpdateCounter;
  }

  @Override
  public void checkIndexConsistancy() {
    fObjectRepository.checkIndexConsistancy();
  }

  @Override
  public void close() {
    fTransactionWriter.close();
  }

  @Override
  public boolean contains(Object obj) {
    return fObjectRepository.contains(obj);
  }

  @Override
  public boolean containsId(IObjectId id) {
    return fObjectRepository.contains(id);
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
    return fObjectRepository.getAllObjectInfos();
  }

  @Override
  public Collection<Object> getAllObjects() {
    return fObjectRepository.getAllObjects();
  }

  public IObjectId getArrayMemoriaClassId() {
    return fObjectRepository.getArrayMemoriaClass();
  }

  @Override
  public IBlockManager getBlockManager() {
    return fTransactionWriter.getBlockManager();
  }

  @Override
  public IDefaultIdProvider getDefaultIdProvider() {
    return fObjectRepository.getIdFactory();
  }

  @Override
  public IObjectId getExistingId(Object obj) {
    return fObjectRepository.getExistingId(obj);
  }

  public IMemoriaFile getFile() {
    return fTransactionWriter.getFile();
  }

  @Override
  public Header getHeader() {
    return fHeader;
  }

  @Override
  public long getHeadRevision() {
    return fTransactionWriter.getHeadRevision();
  }

  @Override
  public IObjectId getId(Object obj) {
    return fObjectRepository.getId(obj);
  }

  @Override
  public int getIdSize() {
    return fObjectRepository.getIdFactory().getIdSize();
  }

  @Override
  public IObjectId getMemoriaArrayClass() {
    return fObjectRepository.getArrayMemoriaClass();
  }

  @Override
  public IMemoriaClass getMemoriaClass(Object object) {
    IObjectId id = getMemoriaClassId(object);
    if(id == null) return null;
    return (IMemoriaClass) fObjectRepository.getObject(id); 
  }
  
  public IMemoriaClass getMemoriaClass(String className) {
    return fObjectRepository.getMemoriaClass(className);
  }

  @Override
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
  @Override
  public <T> T getObject(IObjectId id) {
    return (T) fObjectRepository.getObject(id);
  }

  @Override
  public ObjectInfo getObjectInfo(Object obj) {
    return fObjectRepository.getObjectInfo(obj);
  }

  @Override
  public IObjectInfo getObjectInfoForId(IObjectId id) {
    return fObjectRepository.getObjectInfoForId(id);
  }

  public IObjectRepository getObjectRepo() {
    return fObjectRepository;
  }

  @Override
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
    fModeStrategy.checkCanInstantiateObject(this, memoriaClassId, fDefaultInstantiator);
    ObjectInfo result = fObjectRepository.add(obj, memoriaClassId);
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
