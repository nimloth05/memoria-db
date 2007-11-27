package org.memoriadb.core.meta;

import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.util.ReflectionUtil;

public final class HandlerbasedMemoriaClass extends AbstractMemoriaClass {

  private final ISerializeHandler fSerializeHandler;
  private IMemoriaClass fSuperClass;
  private final IObjectId fMemoriaClassId;

  public HandlerbasedMemoriaClass(ISerializeHandler handler, IObjectId memoriaClassId) {
    fSerializeHandler = handler;
    fMemoriaClassId = memoriaClassId;
  }

  public HandlerbasedMemoriaClass(String handlerName, IObjectId memoriaClassId) throws Exception {
    this(ReflectionUtil.<ISerializeHandler> createInstance(handlerName), memoriaClassId);
  }

  @Override
  public ISerializeHandler getHandler() {
    return fSerializeHandler;
  }

  public String getHandlerName() {
    return fSerializeHandler.getClass().getName();
  }

  public String getJavaClassName() {
    return fSerializeHandler.getClassName();
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
