package org.memoriadb.core.meta;

import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.exception.MemoriaException;

public class HandlerMetaClass implements IMetaClassConfig {
  
  
  private final ISerializeHandler fSerializeHandler;
  private final Class<?> fClazz;
  private IMetaClass fSuperClass;

  public HandlerMetaClass(ISerializeHandler handler, Class<?> clazz) {
    fSerializeHandler = handler;
    fClazz = clazz;
  }
  
  public HandlerMetaClass(String handlerName, String className) throws Exception {
    this((ISerializeHandler) Class.forName(handlerName).newInstance(), Class.forName(className));
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
  public IMetaClass getSuperClass() {
    return fSuperClass;
  }

  @Override
  public Object newInstance() {
    try {
      return fClazz.newInstance();
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }

  @Override
  public void setSuperClass(IMetaClass metaClass) {
    fSuperClass = metaClass;
  }

}
