package org.memoriadb.core.meta;


public abstract class AbstractMemoriaClass implements IMemoriaClassConfig {

  @Override
  public final boolean isTypeFor(String javaClass) {
    if(getJavaClassName().equals(javaClass)) return true;
    IMemoriaClass superClass = getSuperClass();
    if(superClass == null) return false;
    return superClass.isTypeFor(javaClass);
  }
  
}
