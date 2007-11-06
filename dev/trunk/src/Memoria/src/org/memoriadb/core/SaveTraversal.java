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
  private final ObjectStore fMemoria;
  
  public SaveTraversal(ObjectStore memoria) {
    fMemoria = memoria;
  }

  @Override
  public void handle(Object obj) {
    if(fVisited.contains(obj)) return;
    
    fVisited.add(obj);
    fMemoria.internalSave(obj);
    
    fMemoria.getMemoriaClass(obj).getHandler().traverseChildren(obj, this);
  }

}
