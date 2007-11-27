package org.memoriadb.core.meta;

import org.memoriadb.core.handler.IHandler;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.util.ReflectionUtil;

public final class HandlerbasedMemoriaClass extends AbstractMemoriaClass {

  private final IHandler fHandler;
  private IMemoriaClass fSuperClass;
  private final IObjectId fMemoriaClassId;

  public HandlerbasedMemoriaClass(IHandler handler, IObjectId memoriaClassId) {
    fHandler = handler;
    fMemoriaClassId = memoriaClassId;
  }

  public HandlerbasedMemoriaClass(String handlerName, IObjectId memoriaClassId) throws Exception {
    this(ReflectionUtil.<IHandler> createInstance(handlerName), memoriaClassId);
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
  }

}
