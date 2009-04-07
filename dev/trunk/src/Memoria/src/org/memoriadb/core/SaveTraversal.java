package org.memoriadb.core;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.id.IObjectId;



/**
 * 
 * Visits all direct children of the given object to check for enums.
 * 
 * @author msc
 *
 */
public class SaveTraversal implements IObjectTraversal {

  private final TransactionHandler fTransactionHandler;
  
  public SaveTraversal(TransactionHandler transactionHandler) {
    fTransactionHandler = transactionHandler;
  }

  @Override
  public void handle(Object obj) {
    IObjectId memoriaClassId = fTransactionHandler.addMemoriaClassIfNecessary(obj);
    fTransactionHandler.internalSave(obj, memoriaClassId);
    fTransactionHandler.getMemoriaClass(obj).getHandler().traverseChildren(obj, new IObjectTraversal() {

      @Override
      public void handle(Object object) {
        IObjectId classId = fTransactionHandler.addMemoriaClassIfNecessary(object);
        IMemoriaClass clazz = fTransactionHandler.getObject(classId);
        if(clazz.isValueObject()) new AddMemoriaClassesTraversal(fTransactionHandler).handle(object);
      }
      
    });
  }

}
