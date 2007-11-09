package org.memoriadb.core.meta;

import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.util.ReflectionUtil;

public final class MemoriaHandlerClass implements IMemoriaClassConfig {
  
  private final ISerializeHandler fSerializeHandler;
  private final Class<?> fClazz;
  private IMemoriaClass fSuperClass;
  private final IObjectId fMemoriaClassId;

  public MemoriaHandlerClass(ISerializeHandler handler, Class<?> clazz, IObjectId memoriaClassId) {
    fSerializeHandler = handler;
    fClazz = clazz;
    fMemoriaClassId = memoriaClassId;
  }
  
  public MemoriaHandlerClass(String handlerName, String className, IObjectId memoriaClassId) throws Exception {
    this(ReflectionUtil.<ISerializeHandler>createInstance(handlerName), Class.forName(className), memoriaClassId);
  }

  @Override
  public ISerializeHandler getHandler() {
    return fSerializeHandler;
  }

  public String getHandlerName() {
    return fSerializeHandler.getClass().getName();
  }
  
  public String getJavaClassName() {
    return fClazz.getName();
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
  public boolean isTypeFor(String javaClass) {
    return getJavaClassName().equals(javaClass);
  }

  @Override
  public void setSuperClass(IMemoriaClass metaClass) {
    fSuperClass = metaClass;
  }

}
