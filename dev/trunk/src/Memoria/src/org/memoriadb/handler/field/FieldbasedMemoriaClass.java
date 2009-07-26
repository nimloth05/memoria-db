package org.memoriadb.handler.field;

import org.memoriadb.core.meta.AbstractMemoriaClass;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.IHandler;
import org.memoriadb.handler.IHandlerConfig;
import org.memoriadb.id.IObjectId;

public final class FieldbasedMemoriaClass extends AbstractMemoriaClass {

  private IHandler fHandler;
  private IMemoriaClass fSuperClass;
  private final IObjectId fMemoriaClassId;

  public FieldbasedMemoriaClass(FieldbasedObjectHandler handler, IObjectId memoriaClassId, boolean isValueObject) {
    super(isValueObject);
    this.fHandler = handler;
    fMemoriaClassId = memoriaClassId;
  }

  /**
   * Constructor for deserialization.
   */
  public FieldbasedMemoriaClass(String className, IObjectId memoriaClassId, boolean isValueObject) {
    super(isValueObject);
    fMemoriaClassId = memoriaClassId;
  }

  @Override
  public IHandler getHandler() {
    return fHandler;
  }

  @Override
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

  public void setSuperClass(IMemoriaClass superClass) {
    fSuperClass = superClass;
    if (fHandler instanceof IHandlerConfig) {
      ((IHandlerConfig)fHandler).setSuperClass(fSuperClass);
    }
  }

  @Override
  public String toString() {
    return getJavaClassName();
  }

}