package org.memoriadb.core.mode;

import java.util.List;

import org.memoriadb.*;
import org.memoriadb.core.TransactionHandler;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.query.ObjectModeQueryStrategy;
import org.memoriadb.id.IObjectId;

public class ObjectStore extends AbstractStore implements IObjectStore  {

  private final ObjectModeQueryStrategy fQueryStrategy = new ObjectModeQueryStrategy();

  public ObjectStore(TransactionHandler handler) {
    super(handler);
  }
  
  @Override
  public boolean contains(Object obj) {
    if(obj == null) return false;
    return fTransactionHandler.contains(obj);
  }

  @Override
  public void delete(Object obj) {
    if(obj == null) throw new MemoriaException("can not delete null");
    fTransactionHandler.delete(obj);
  }

  @Override
  public void deleteAll(Object root) {
    if(root == null) throw new MemoriaException("can not delete null");
    fTransactionHandler.deleteAll(root);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T get(IObjectId id) {
    if(id== null) throw new MemoriaException("id was null");
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
    if(obj == null) throw new MemoriaException("null has no id");
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
    if(obj == null) throw new MemoriaException("can not save null");
    return fTransactionHandler.save(obj);
  }

  public IObjectId saveAll(Object root) {
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
