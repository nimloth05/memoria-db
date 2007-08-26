package org.memoriadb.core;

import org.memoriadb.core.meta.IMetaClass;
import org.memoriadb.exception.MemoriaException;

public abstract class MetaClassInheritanceTraverser {
  
  public MetaClassInheritanceTraverser(IMetaClass startClass) {
    if (startClass == null) throw new IllegalArgumentException("startClass can not be null");
    traverse(startClass);
  }
  
  protected abstract void handle(IMetaClass metaObject) throws Exception;

  private void internalHandle(IMetaClass classObject) {
    try {
      handle(classObject);
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }

  private void traverse(IMetaClass startClass) {
    IMetaClass classObject = startClass;
    while (true) {
      internalHandle(classObject);
      
      if (classObject.getSuperClass() == null && !classObject.getJavaClass().equals(Object.class)) throw new MemoriaException("Class Hierarchy is corrupt. Only the java Object MetaClass can have no super class. " + this);
      classObject = classObject.getSuperClass();
      if (classObject == null) break;
    }
  }

}
