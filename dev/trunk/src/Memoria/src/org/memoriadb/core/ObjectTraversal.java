package org.memoriadb.core;

import java.util.Set;

import org.memoriadb.util.IdentityHashSet;

public class ObjectTraversal implements IObjectTraversal {

  private final Set<Object> fVisited = new IdentityHashSet<Object>();
  private final ObjectStore fMemoria;
  
  public ObjectTraversal(ObjectStore memoria) {
    fMemoria = memoria;
  }

  @Override
  public void handle(Object obj) {
    if(fVisited.contains(obj)) return;
    
    fVisited.add(obj);
    fMemoria.internalSave(obj);
    
    fMemoria.getMetaClass(obj).getHandler().traverseChildren(obj, this);
  }

}
