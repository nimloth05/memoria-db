package org.memoriadb.core;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.id.IObjectId;

public class AddMemoriaClassesTraversal implements IObjectTraversal {
  
  private final TransactionHandler fTransactionHandler;

  public AddMemoriaClassesTraversal(TransactionHandler transactionHandler) {
    fTransactionHandler = transactionHandler;
  }

  @Override
  public void handle(Object obj) {
    IObjectId classId = fTransactionHandler.addMemoriaClassIfNecessary(obj);
    IMemoriaClass memoriaClass = fTransactionHandler.getObject(classId);
    memoriaClass.getHandler().traverseChildren(obj, this);
  }
  
  

}
