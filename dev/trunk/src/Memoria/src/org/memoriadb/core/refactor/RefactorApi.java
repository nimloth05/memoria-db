package org.memoriadb.core.refactor;

import org.memoriadb.IRefactor;
import org.memoriadb.core.TransactionHandler;
import org.memoriadb.core.handler.IDataObject;
import org.memoriadb.core.handler.array.*;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.Type;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.*;

public class RefactorApi implements IRefactor {
  
  private final TransactionHandler fTransactionHandler;

  public RefactorApi(TransactionHandler transactionHandler) {
    fTransactionHandler = transactionHandler;
  }

  @Override
  public void arraySet(int i, Object obj) {
    // TODO Auto-generated method stub
    
  }

  public IArray createArray(Class<?> klass, int length) {
    if(!klass.isArray()) throw new MemoriaException("not an array " + klass);
    return new DataArray(getArrayClass(), ReflectionUtil.getComponentTypeInfo(klass), length);
  }

  @Override
  public IArray createArray(String componentType, int dimension, int length) {
    ArrayTypeInfo info = new ArrayTypeInfo(Type.typeClass, dimension, componentType);
    return new DataArray(getArrayClass(), info, length);
  }

  @Override
  public IDataObject createObject() {
    // TODO Auto-generated method stub
    return null;
  }

  private IObjectId getArrayClass() {
    return fTransactionHandler.getIdFactory().getArrayMemoriaClass();
  }
  
}
