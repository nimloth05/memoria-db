package org.memoriadb.core.meta;

import org.memoriadb.handler.IHandler;
import org.memoriadb.handler.IHandlerConfig;
import org.memoriadb.id.IObjectId;

public final class MemoriaClass implements IMemoriaClassConfig {

  private final IHandler fHandler;
  private IMemoriaClass fSuperClass;
  private final IObjectId fMemoriaClassId;
  private boolean fIsValueObject;

  public MemoriaClass(IHandler handler, IObjectId memoriaClassId, boolean isValueObject) {
    fIsValueObject = isValueObject;
    fHandler = handler;
    fMemoriaClassId = memoriaClassId;
  }

  @Override
  public IHandler getHandler() {
    return fHandler;
  }

  public String getHandlerName() {
    return fHandler.getClass().getName();
  }

  public String getJavaClassName() {
    return fHandler.getClassName();
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fMemoriaClassId;
  }

  @Override
  public IMemoriaClass getSuperClass() {
    return fSuperClass;
  }

  @Override
  public void setSuperClass(IMemoriaClass metaClass) {
    fSuperClass = metaClass;
    if (fHandler instanceof IHandlerConfig) {
      ((IHandlerConfig)fHandler).setSuperClass(fSuperClass);
    }
  }

  @Override
  public String toString() {
    return "javaClass: "+ getJavaClassName() + " handler: " + getHandlerName();
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
