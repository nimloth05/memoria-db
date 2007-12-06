package org.memoriadb.core.meta;

import org.memoriadb.handler.IHandler;
import org.memoriadb.id.IObjectId;

public final class HandlerbasedMemoriaClass extends AbstractMemoriaClass {

  private final IHandler fHandler;
  private IMemoriaClass fSuperClass;
  private final IObjectId fMemoriaClassId;
  private final boolean fHasValueObjectAnnotation;

  public HandlerbasedMemoriaClass(IHandler handler, IObjectId memoriaClassId, boolean hasValueObjectAnnotation) {
    fHandler = handler;
    fMemoriaClassId = memoriaClassId;
    fHasValueObjectAnnotation = hasValueObjectAnnotation;
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
  public boolean hasValueObjectAnnotation() {
    return fHasValueObjectAnnotation;
  }

  @Override
  public void setSuperClass(IMemoriaClass metaClass) {
    fSuperClass = metaClass;
  }

  @Override
  public String toString() {
    return "javaClass: "+ getJavaClassName() + " handler: " + getHandlerName();
  }

}
