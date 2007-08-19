package org.memoriadb.core.facade.nternal;

import java.util.Set;

import org.memoriadb.core.facade.IMemoria;
import org.memoriadb.util.IdentityHashSet;

public class ObjectTraversal implements IObjectTraversal {

  private final Set<Object> fVisited = new IdentityHashSet<Object>();
  private final IMemoria fMemoria;
  
  public ObjectTraversal(IMemoria memoria) {
    fMemoria = memoria;
  }

  @Override
  public void visit(Object obj) {
    if(fVisited.contains(obj)) return;
    
    fVisited.add(obj);
    fMemoria.save(obj);
    
    fMemoria.getMetaClass(obj).getHandler().traverseChildren(obj, this);
  }

}
