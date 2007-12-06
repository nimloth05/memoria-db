package org.memoriadb.core.mode;

import java.util.List;

import org.memoriadb.*;
import org.memoriadb.block.*;
import org.memoriadb.core.*;
import org.memoriadb.core.block.SurvivorAgent;
import org.memoriadb.core.file.*;
import org.memoriadb.core.meta.IMemoriaClassConfig;
import org.memoriadb.core.query.ObjectModeQueryStrategy;
import org.memoriadb.id.*;

public class ObjectStore implements IObjectStoreExt  {

  // FIXME hier sollte die Schnittstelle von TransactionHandler verwendet werden, geht aber nicht.
  // bringt die Schnittstelle überhaupt etwas?
  private final TransactionHandler fTransactionHandler;
  
  private final ObjectModeQueryStrategy fQueryStrategy = new ObjectModeQueryStrategy();
  
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
  public <T> T get(IObjectId id) {
    return (T) fTransactionHandler.getObject(id);
  }

  @Override
  public Iterable<Object> getAllObjects() {
    return fTransactionHandler.getAllObjects();
  }

  @Override
  public Iterable<Object> getAllUserSpaceObjects() {
    return fTransactionHandler.getAllUserSpaceObjects();
  }

  @Override
  public IBlockManager getBlockManager() {
    return fTransactionHandler.getBlockManager();
  }

  public IMemoriaFile getFile() {
    return fTransactionHandler.getFile();
  }

  @Override
  public Header getHeader() {
    return fTransactionHandler.getHeader();
  }

  @Override
  public long getHeadRevision() {
    return fTransactionHandler.getHeadRevision();
  }

  @Override
  public IObjectId getId(Object obj) {
    return fTransactionHandler.getId(obj);
  }

  @Override
  public IIdProvider getIdFactory() {
    return fTransactionHandler.getDefaultIdProvider();
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
  public SurvivorAgent getSurvivorAgent(Block block) {
    return fTransactionHandler.getSurvivorAgent(block);
  }

  @Override
  public ITypeInfo getTypeInfo() {
    return fTransactionHandler.getTypeInfo();
  }

  @Override
  public boolean isInUpdateMode() {
    return fTransactionHandler.isInUpdateMode();
  } 

  @SuppressWarnings("unchecked")
  @Override
  public <T> List<T> query(Class<T> clazz) {
    return fQueryStrategy.query(fTransactionHandler.getObjectRepo(), clazz);
  }

  @Override
  public <FILTER, T extends FILTER> List<T> query(Class<T> clazz, IFilter<FILTER> filter) {
    return fQueryStrategy.query(fTransactionHandler.getObjectRepo(), clazz, filter);
  }
  
  @Override
  public <T> List<T> query(String clazz) {
    return fQueryStrategy.query(fTransactionHandler.getObjectRepo(), clazz);
  }
  
  @Override
  public List<Object> query(String clazz, IFilter<Object> filter) {
    return fQueryStrategy.query(fTransactionHandler.getObjectRepo(), clazz);
  }
  
  public IObjectId save(Object obj) {
    return fTransactionHandler.save(obj);
  }

  public IObjectId saveAll(Object root) {
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

}
