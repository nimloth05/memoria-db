package org.memoriadb.test.testclasses.weakref;

import org.memoriadb.WeakRef;

public class WeakOwner {
  
  @WeakRef
  private Object fWeakRef;

  public WeakOwner() {
  }
  
  public WeakOwner(Object weakRef) {
    fWeakRef = weakRef;
  }

  public Object getWeakRef() {
    return fWeakRef;
  }
  
}
