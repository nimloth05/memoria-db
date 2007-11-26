package org.memoriadb.core;

import org.memoriadb.ITypeInfo;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.IMemoriaClass;

public class TypeInfo implements ITypeInfo {
 
  private final ITransactionHandler fTransactionHandler;

  public TypeInfo(ITransactionHandler transactionHandler) {
    fTransactionHandler = transactionHandler;
  }

  @Override
  public IObjectId addMemoriaClass(Class<?> clazz) {
    return fTransactionHandler.addMemoriaClass(clazz);
  }
  
  @Override
  public IObjectId getMemoriaArrayClass() {
    return fTransactionHandler.getMemoriaArrayClass();
  }

  @Override
  public IMemoriaClass getMemoriaClass(Class<?> clazz) {
    return fTransactionHandler.getMemoriaClass(clazz.getName());
  }

  @Override
  public IMemoriaClass getMemoriaClass(Object object) {
    return fTransactionHandler.getMemoriaClass(object);
  }

  @Override
  public IMemoriaClass getMemoriaClass(String className) {
    return fTransactionHandler.getMemoriaClass(className);
  }

  @Override
  public IObjectId getMemoriaClassId(Class<?> clazz) {
    return getMemoriaClassId(clazz.getName());
  }

  @Override
  public IObjectId getMemoriaClassId(Object object) {
    return fTransactionHandler.getMemoriaClassId(object);
  }

  @Override
  public IObjectId getMemoriaClassId(String className) {
    return fTransactionHandler.getMemoriaClassId(className);
  }

}
