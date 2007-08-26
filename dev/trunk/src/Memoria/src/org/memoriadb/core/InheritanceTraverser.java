package org.memoriadb.core;

/**
 * This class is a iterator which can traverse a clazz hierrachy. The first class which will be handled is the given startClass. 
 * 
 * @author sandro
 *
 */
public abstract class InheritanceTraverser {
  
  
  private boolean fAborted;

  public InheritanceTraverser(Class<?> startClazz) {
    traverse(startClazz);
  }

  protected final void abort() {
    fAborted = true;
  }
  
  protected abstract void handle(Class<?> clazz);
  
  private void traverse(Class<?> startClazz) {
    Class<?> clazz = startClazz;
    while (clazz != null) {
      handle(clazz);
      clazz = clazz.getSuperclass();
      if (fAborted) break;
    }
  }
  

}
