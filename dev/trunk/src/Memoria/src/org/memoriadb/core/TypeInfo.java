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
    return fTransactionHandler.getMemoriaClass(clazz);
  }

  @Override
  public IMemoriaClass getMemoriaClass(Object obj) {
    IObjectId memoriaClassId = getMemoriaClassId(obj);
    if(memoriaClassId == null) return null;
    return fTransactionHandler.getObject(memoriaClassId);
  }

  @Override
  public IObjectId getMemoriaClassId(Class<?> clazz) {
    return fTransactionHandler.getMemoriaClassId(clazz);
  }

  @Override
  public IObjectId getMemoriaClassId(Object obj) {
    IObjectInfo objectInfo = fTransactionHandler.getObjectInfo(obj);
    if(objectInfo == null) return null;
    return objectInfo.getMemoriaClassId();
  }

}
