package org.memoriadb.core.mode;

import java.util.List;

import org.memoriadb.*;
import org.memoriadb.core.TransactionHandler;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.meta.IMemoriaClassConfig;
import org.memoriadb.core.query.DataModeQueryStrategy;
import org.memoriadb.core.refactor.RefactorApi;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.*;

public class DataStore extends AbstractStore implements IDataStore {

  private final DataModeQueryStrategy fQueryStrategy = new DataModeQueryStrategy();

  public DataStore(TransactionHandler handler) {
    super(handler);
  }
  
  @Override
  public boolean contains(IDataObject obj) {
    if(obj == null) return false;
    return fTransactionHandler.contains(obj);
  }

  @Override
  public void delete(IDataObject obj) {
    if(obj == null) throw new MemoriaException("can not delete null");
    fTransactionHandler.delete(obj);
  }

  @Override
  public void deleteAll(IDataObject root) {
    if(root == null) throw new MemoriaException("can not delete null");
    fTransactionHandler.deleteAll(root);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends IDataObject> T  get(IObjectId id) {
    if(id== null) throw new MemoriaException("id was null");
    return (T) fTransactionHandler.getObject(id);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Iterable<IDataObject> getAllObjects() {
    Iterable<?> result= fTransactionHandler.getAllObjects();
    return (Iterable<IDataObject>) result;
  }

  @Override
  public IIdProvider getDefaultIdProvider() {
    return fTransactionHandler.getDefaultIdProvider();
  }

  @Override
  public IObjectId getId(IDataObject obj) {
    if(obj == null) throw new MemoriaException("null has no id");
    return fTransactionHandler.getId(obj);
  }

  @Override
  public IRefactor getRefactorApi() {
    return new RefactorApi(this); 
  }
  
  public IMemoriaClassConfig internalGetMemoriaClass(String klass) {
    return fTransactionHandler.internalGetMemoriaClass(klass);
  }
 
  public <T extends IDataObject> List<T> query(String clazz) {
    return fQueryStrategy.query(fTransactionHandler.getObjectRepo(), clazz);
  }

  public <T extends IDataObject> List<T> query(String clazz, IFilter<T> filter) {
    return fQueryStrategy.query(fTransactionHandler.getObjectRepo(), clazz, filter);
  }

  public IObjectId save(IDataObject obj) {
    if(obj == null) throw new MemoriaException("can not save null");
    return fTransactionHandler.save(obj);
  }

  public IObjectId saveAll(IDataObject root) {
    if(root == null) throw new MemoriaException("can not save null");
    return fTransactionHandler.saveAll(root);
  }

  public void writePendingChanges() {
    fTransactionHandler.writePendingChanges();
  }
  
  void internalDelete(Object obj) {
    fTransactionHandler.internalDelete(obj);
  }
}
