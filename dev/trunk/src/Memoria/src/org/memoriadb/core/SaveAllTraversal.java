package org.memoriadb.core;

import java.util.Set;

import org.memoriadb.core.util.IdentityHashSet;

/**
 * 
 * Traverses a given object graph and saves all visited objects
 * 
 * @author msc
 *
 */
public class SaveAllTraversal implements IObjectTraversal {

  private final Set<Object> fVisited = new IdentityHashSet<Object>();
  private final TransactionHandler fTransactionHandler;
  
  public SaveAllTraversal(TransactionHandler transactionHandler) {
    fTransactionHandler = transactionHandler;
  }

  @Override
  public void handle(Object obj) {
    if(fVisited.contains(obj)) return;
    fVisited.add(obj);
    
    if(fTransactionHandler.isEnum(obj)) {
      fTransactionHandler.addMemoriaClassIfNecessary(obj);
      return;
    }
    
    if (fTransactionHandler.isValueObject(obj)) {
      fTransactionHandler.addMemoriaClassIfNecessary(obj);
      return;
    }
    
    fTransactionHandler.internalSave(obj);
    fTransactionHandler.getMemoriaClass(obj).getHandler().traverseChildren(obj, this);
  }

}
