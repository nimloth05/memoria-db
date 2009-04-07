package org.memoriadb.core;

import java.util.Set;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.util.collection.identity.IdentityHashSet;
import org.memoriadb.id.IObjectId;

/**
 * 
 * Traverses a given object graph and saves all visited objects
 * 
 * @author msc
 *
 */
public class SaveAllTraversal implements IObjectTraversal {

  private final Set<Object> fVisited = IdentityHashSet.create();
  private final TransactionHandler fTransactionHandler;
  
  public SaveAllTraversal(TransactionHandler transactionHandler) {
    fTransactionHandler = transactionHandler;
  }

  @Override
  public void handle(Object obj) {
    if(fVisited.contains(obj)) return;
    fVisited.add(obj);
    
    IObjectId memoriaClassId = fTransactionHandler.addMemoriaClassIfNecessary(obj);
    IMemoriaClass clazz = fTransactionHandler.getObject(memoriaClassId);
    
    if(fTransactionHandler.isEnum(obj)) return;
    if(clazz.isValueObject()) {
      new AddMemoriaClassesTraversal(fTransactionHandler).handle(obj);
      return;
    }
    
    fTransactionHandler.internalSave(obj, memoriaClassId);
    fTransactionHandler.getMemoriaClass(obj).getHandler().traverseChildren(obj, this);
  }

}
