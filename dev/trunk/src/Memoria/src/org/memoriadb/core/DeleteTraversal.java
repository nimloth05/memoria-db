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
public class DeleteTraversal implements IObjectTraversal {

  private final Set<Object> fVisited = new IdentityHashSet<Object>();
  private final ObjectStore fObjectStore;
  
  public DeleteTraversal(ObjectStore memoria) {
    fObjectStore = memoria;
  }

  @Override
  public void handle(Object obj) {
    if(fVisited.contains(obj)) return;
    
    fVisited.add(obj);
    fObjectStore.internalDelete(obj);
    
    fObjectStore.getMemoriaClass(obj).getHandler().traverseChildren(obj, this);
  }

}
