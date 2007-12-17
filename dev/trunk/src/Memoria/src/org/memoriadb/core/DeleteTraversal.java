package org.memoriadb.core;

import java.util.Set;

import org.memoriadb.core.meta.IMemoriaClass;
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
    fVisited.add(obj);

    if(fTransactionHandler.isEnum(obj)) return;
    
    IMemoriaClass memoriaClass = fTransactionHandler.getMemoriaClass(obj);
    if(memoriaClass == null) return;
    if(memoriaClass.isValueObject()) return;
    
    
    memoriaClass.getHandler().traverseChildren(obj, this);
    fTransactionHandler.internalDelete(obj);
  }

}
