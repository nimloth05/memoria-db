package org.memoriadb.core;

import java.util.Set;

import org.memoriadb.util.IdentityHashSet;

/**
 * 
 * Traverses a given object graph and saves all visited objects
 * 
 * @author msc
 *
 */
public class SaveTraversal implements IObjectTraversal {

  private final Set<Object> fVisited = new IdentityHashSet<Object>();
  private final TrxHandler fTrxHandler;
  
  public SaveTraversal(TrxHandler trxHandler) {
    fTrxHandler = trxHandler;
  }

  @Override
  public void handle(Object obj) {
    if(fVisited.contains(obj)) return;
    
    fVisited.add(obj);
    fTrxHandler.internalSave(obj);
    
    fTrxHandler.getMemoriaClass(obj).getHandler().traverseChildren(obj, this);
  }

}
