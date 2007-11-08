package org.memoriadb.core;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.exception.MemoriaException;

public abstract class MetaClassInheritanceTraverser {
  
  public MetaClassInheritanceTraverser(IMemoriaClass startClass) {
    if (startClass == null) throw new IllegalArgumentException("startClass can not be null");
    traverse(startClass);
  }
  
  protected abstract void handle(IMemoriaClass metaObject) throws Exception;

  private void internalHandle(IMemoriaClass classObject) {
    try {
      handle(classObject);
    }
    catch (Exception e) {
      throw new MemoriaException("Exception during object traversal. classObject for JavaClass "+classObject.getJavaClassName(), e);
    }
  }

  private void traverse(IMemoriaClass startClass) {
    IMemoriaClass classObject = startClass;
    while (true) {
      internalHandle(classObject);
      
      if (classObject.getSuperClass() == null && !classObject.getJavaClassName().equals(Object.class.getName())) throw new MemoriaException("Class Hierarchy is corrupt. Only the java Object MetaClass can have no super class. " + this);
      classObject = classObject.getSuperClass();
      if (classObject == null) break;
    }
  }

}
