package org.memoriadb.core.meta;


public abstract class AbstractMemoriaClass implements IMemoriaClassConfig {

  private boolean fIsValueObject;
  
  public AbstractMemoriaClass(boolean isValueObject) {
    this.fIsValueObject = isValueObject;
  }
  
  @Override
  public final boolean isTypeFor(String javaClass) {
    if(getJavaClassName().equals(javaClass)) return true;
    IMemoriaClass superClass = getSuperClass();
    if(superClass == null) return false;
    return superClass.isTypeFor(javaClass);
  }
  
  @Override
  public boolean isValueObject() {
    return fIsValueObject;
  }
  
  public void setValueObject(boolean value) {
    fIsValueObject = value;
  }

}
