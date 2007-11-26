package org.memoriadb.core.mode;

import java.util.*;

import org.memoriadb.*;
import org.memoriadb.core.*;
import org.memoriadb.core.block.*;
import org.memoriadb.core.file.*;
import org.memoriadb.core.handler.IDataObject;
import org.memoriadb.core.id.*;
import org.memoriadb.core.meta.IMemoriaClassConfig;
import org.memoriadb.core.query.DataModeQueryStrategy;
import org.memoriadb.core.refactor.RefactorApi;

public class DataStore implements IDataStoreExt {

  private final TransactionHandler fTransactionHandler;
  private final DataModeQueryStrategy fQueryStrategy = new DataModeQueryStrategy();
  
  public DataStore(TransactionHandler transactionHandler) {
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
  public boolean contains(IDataObject obj) {
    return fTransactionHandler.contains(obj);
  }

  @Override
  public boolean containsId(IObjectId id) {
    return fTransactionHandler.contains(id);
  }

  @Override
  public void delete(IDataObject obj) {
    fTransactionHandler.delete(obj);
  }

  @Override
  public void deleteAll(IDataObject root) {
    fTransactionHandler.deleteAll(root);
  }

  @Override
  public void endUpdate() {
    fTransactionHandler.endUpdate();
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public <T extends IDataObject> T  get(IObjectId id) {
    return (T) fTransactionHandler.getObject(id);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Collection<IDataObject> getAllObjects() {
    Collection<?> result= fTransactionHandler.getAllObjects();
    return (Collection<IDataObject>) result;
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
  public IObjectId getId(IDataObject obj) {
    return fTransactionHandler.getId(obj);
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
  public ObjectInfo getObjectInfo(Object obj) {
    return fTransactionHandler.getObjectInfo(obj);
  }

  @Override
  public IObjectInfo getObjectInfoForId(IObjectId id) {
    return fTransactionHandler.getObjectInfoForId(id);
  }

  @Override
  public IRefactor getRefactorApi() {
    return new RefactorApi(fTransactionHandler); 
  }

  @Override
  public Set<ObjectInfo> getSurvivors(Block block) {
    return fTransactionHandler.getSurvivors(block);
  }


  @Override
  public ITypeInfo getTypeInfo() {
    return new TypeInfo(fTransactionHandler);
  }
  
  
  public IMemoriaClassConfig internalGetMemoriaClass(String klass) {
    return fTransactionHandler.internalGetMemoriaClass(klass);
  }
  
  @Override
  public boolean isInUpdateMode() {
    return fTransactionHandler.isInUpdateMode();
  }
  
  public <T extends IDataObject> List<T> query(String clazz) {
    return fQueryStrategy.query(fTransactionHandler.getObjectRepo(), clazz);
  }


  public <T extends IDataObject> List<T> query(String clazz, IFilter<T> filter) {
    return fQueryStrategy.query(fTransactionHandler.getObjectRepo(), clazz, filter);
  }
 
  public IObjectId save(IDataObject obj) {
    return fTransactionHandler.save(obj);
  }

  public IObjectId saveAll(IDataObject root) {
    return fTransactionHandler.saveAll(root);
  }


  public void writePendingChanges() {
    fTransactionHandler.writePendingChanges();
  }


  void internalDelete(Object obj) {
    fTransactionHandler.internalDelete(obj);
  }

  /**
   * Saves the obj without considering if this ObjectStore is in update-mode or not.
   */
  IObjectId internalSave(Object obj) {
    return fTransactionHandler.internalSave(obj);
  }
  
}
