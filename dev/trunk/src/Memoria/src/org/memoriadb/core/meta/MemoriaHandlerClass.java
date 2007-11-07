package org.memoriadb.core.meta;

import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.util.ReflectionUtil;

public final class MemoriaHandlerClass implements IMemoriaClassConfig {
  
  
  private final ISerializeHandler fSerializeHandler;
  private final Class<?> fClazz;
  private IMemoriaClass fSuperClass;

  public MemoriaHandlerClass(ISerializeHandler handler, Class<?> clazz) {
    fSerializeHandler = handler;
    fClazz = clazz;
  }
  
  public MemoriaHandlerClass(String handlerName, String className) throws Exception {
    this(ReflectionUtil.<ISerializeHandler>createInstance(handlerName), Class.forName(className));
  }

  @Override
  public ISerializeHandler getHandler() {
    return fSerializeHandler;
  }

  public String getHandlerName() {
    return fSerializeHandler.getClass().getName();
  }
  
  @Override
  public Class<?> getJavaClass() {
    return fClazz;
  }

  public String getJavaClassName() {
    return fClazz.getName();
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
