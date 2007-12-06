package org.memoriadb.core;



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
    fTransactionHandler.internalSave(obj);
    
    fTransactionHandler.getMemoriaClass(obj).getHandler().traverseChildren(obj, new IObjectTraversal() {

      @Override
      public void handle(Object object) {
        if (fTransactionHandler.isEnum(object)) fTransactionHandler.addMemoriaClassIfNecessary(object);
        if (fTransactionHandler.isValueObject(object)) fTransactionHandler.addMemoriaClassIfNecessary(object); 
      }
      
    });
  }

}
