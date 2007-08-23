package org.memoriadb.core;

import java.util.Set;

import org.memoriadb.IObjectContainer;
import org.memoriadb.util.IdentityHashSet;

public class ObjectTraversal implements IObjectTraversal {

  private final Set<Object> fVisited = new IdentityHashSet<Object>();
  private final IObjectContainer fMemoria;
  
  public ObjectTraversal(IObjectContainer memoria) {
    fMemoria = memoria;
  }

  @Override
  public void handle(Object obj) {
    if(fVisited.contains(obj)) return;
    
    fVisited.add(obj);
    fMemoria.save(obj);
    
    fMemoria.getMetaClass(obj).getHandler().traverseChildren(obj, this);
  }

}
