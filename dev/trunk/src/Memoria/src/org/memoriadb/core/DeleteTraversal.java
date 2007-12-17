package org.memoriadb.core;

import java.util.Set;

import org.memoriadb.core.util.collection.identity.IdentityHashSet;

/**
 * 
 * Traverses a given object graph and saves all visited objects
 * 
 * @author msc
 *
 */
public class DeleteTraversal implements IObjectTraversal {

  private final Set<Object> fVisited = IdentityHashSet.create();
  private final TransactionHandler fTransactionHandler;
  
  public DeleteTraversal(TransactionHandler transactionHandler) {
    fTransactionHandler = transactionHandler;
  }

  @Override
  public void handle(Object obj) {
    if(fVisited.contains(obj)) return;
    if(fTransactionHandler.isEnum(obj)) return;
    if(!fTransactionHandler.contains(obj)){
      System.out.println("deleting nonexisting object: " + obj);
      return;
    }
    
    fVisited.add(obj);
    
    
    fTransactionHandler.getMemoriaClass(obj).getHandler().traverseChildren(obj, this);
    fTransactionHandler.internalDelete(obj);
  }

}
