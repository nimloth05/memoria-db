package org.memoriadb.core.mode;

import java.util.List;

import org.memoriadb.*;
import org.memoriadb.core.TransactionHandler;
import org.memoriadb.core.query.ObjectModeQueryStrategy;
import org.memoriadb.id.IObjectId;

public class ObjectStore extends AbstractStore implements IObjectStore  {

  private final ObjectModeQueryStrategy fQueryStrategy = new ObjectModeQueryStrategy();

  public ObjectStore(TransactionHandler handler) {
    super(handler);
  }
  
  @Override
  public boolean contains(Object obj) {
    return fTransactionHandler.contains(obj);
  }

  @Override
  public void delete(Object obj) {
    fTransactionHandler.delete(obj);
  }

  @Override
  public void deleteAll(Object root) {
    fTransactionHandler.deleteAll(root);
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
  public IObjectId getId(Object obj) {
    return fTransactionHandler.getId(obj);
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

//  /* package */ IMemoriaClassConfig internalGetMemoriaClass(String klass) {
//    return fTransactionHandler.internalGetMemoriaClass(klass);
//  }

}
