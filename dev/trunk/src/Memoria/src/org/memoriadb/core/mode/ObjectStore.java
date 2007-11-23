package org.memoriadb.core.mode;

import java.util.*;

import org.memoriadb.IFilter;
import org.memoriadb.core.*;
import org.memoriadb.core.block.*;
import org.memoriadb.core.file.*;
import org.memoriadb.core.handler.IDataObject;
import org.memoriadb.core.id.*;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.query.*;
import org.memoriadb.exception.*;
import org.memoriadb.util.ReflectionUtil;

public class ObjectStore implements IObjectStoreExt  {

  // FIXME hier sollte die Schnittstelle von TransactionHandler verwendet werden, geht aber nicht.
  // bringt die Schnittstelle überhaupt etwas?
  private final TransactionHandler fTransactionHandler;
  
  private final IQueryStrategy fQueryStrategy = new ClassModeQueryStrategy();
  
  public ObjectStore(TransactionHandler transactionHandler) {
    fTransactionHandler = transactionHandler;
  }

  @Override
  public void beginUpdate() {
    fTransactionHandler.beginUpdate();
  }

  @Override
  public void checkIndexConsistancy() {
    fTransactionHandler.checkIndexConsistancy();
  }

  @Override
  public void close() {
    fTransactionHandler.close();
  }

  @Override
  public boolean contains(Object obj) {
    return fTransactionHandler.contains(obj);
  }

  @Override
  public boolean containsId(IObjectId id) {
    return fTransactionHandler.containsId(id);
  }

  @Override
  public void delete(Object obj) {
    fTransactionHandler.delete(obj);
  }

  @Override
  public void deleteAll(Object root) {
    fTransactionHandler.deleteAll(root);
  }

  @Override
  public void endUpdate() {
    fTransactionHandler.endUpdate();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> List<T> getAll(Class<T> clazz) {
    return fQueryStrategy.getAll(fTransactionHandler.getObjectRepo(), clazz);
  }

  @Override
  public <T> List<T> getAll(Class<T> clazz, IFilter<T> filter) {
    return fQueryStrategy.getAll(fTransactionHandler.getObjectRepo(), clazz, filter);
  }

  @Override
  public List<Object> getAll(String clazz) {
    return fQueryStrategy.getAll(fTransactionHandler.getObjectRepo(), clazz);
  }

  @Override
  public List<Object> getAll(String clazz, IFilter<Object> filter) {
    return fQueryStrategy.getAll(fTransactionHandler.getObjectRepo(), clazz, filter);
  }

  @Override
  public Collection<Object> getAllObjects() {
    return fTransactionHandler.getAllObjects();
  }

  @Override
  public IBlockManager getBlockManager() {
    return fTransactionHandler.getBlockManager();
  }

  public IMemoriaFile getFile() {
    return fTransactionHandler.getFile();
  }

  @Override
  public FileHeader getHeader() {
    return fTransactionHandler.getHeader();
  }

  @Override
  public long getHeadRevision() {
    return fTransactionHandler.getHeadRevision();
  }

  @Override
  public IDefaultObjectIdProvider getIdFactory() {
    return fTransactionHandler.getIdFactory();
  }

  @Override
  public int getIdSize() {
    return fTransactionHandler.getIdSize();
  }

  @Override
  public IMemoriaClass getMemoriaClass(Class<?> clazz) {
    return fTransactionHandler.getMemoriaClass(clazz);
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
    return fTransactionHandler.getMemoriaFieldMetaClass();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getObject(IObjectId id) {
    return (T) fTransactionHandler.getObject(id);
  }

  @Override
  public IObjectId getObjectId(Object obj) {
    return fTransactionHandler.getObjectId(obj);
  }

  @Override
  public ObjectInfo getObjectInfo(Object obj) {
    return fTransactionHandler.getObjectInfo(obj);
  }

  @Override
  public IObjectInfo getObjectInfoForId(IObjectId id) {
    return fTransactionHandler.getObjectInfoForId(id);
  }

  @Override
  public Set<ObjectInfo> getSurvivors(Block block) {
    return fTransactionHandler.getSurvivors(block);
  }


  @Override
  public boolean isInUpdateMode() {
    return fTransactionHandler.isInUpdateMode();
  }

  public IObjectId save(Object obj) {
    checkObject(obj);
    return fTransactionHandler.save(obj);
  }

  public IObjectId saveAll(Object root) {
    checkObject(root);
    return fTransactionHandler.saveAll(root);
  }

  public void writePendingChanges() {
    fTransactionHandler.writePendingChanges();
  }
  
  
  void internalDelete(Object obj) {
    fTransactionHandler.internalDelete(obj);
  }
  
  /* package */ IMemoriaClassConfig internalGetMemoriaClass(String klass) {
    return fTransactionHandler.internalGetMemoriaClass(klass);
  }
  
  /**
   * Saves the obj without considering if this ObjectStore is in update-mode or not.
   */
  IObjectId internalSave(Object obj) {
    return fTransactionHandler.internalSave(obj);
  }


  private void checkObject(Object obj) {
    if(obj instanceof IDataObject) throw new MemoriaException("IDataObjects are for data-mode only: " + obj);
    
    if (ReflectionUtil.isNonStaticInnerClass(obj.getClass())) {
      throw new SchemaException("cannot save non-static inner classes " + obj.getClass());
    }
    
    if(Type.isPrimitive(obj)) throw new MemoriaException("cannot save primitive");
    
  }

}
