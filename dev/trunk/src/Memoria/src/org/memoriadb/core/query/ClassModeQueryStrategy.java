package org.memoriadb.core.query;

import java.lang.reflect.Method;
import java.util.List;

import org.memoriadb.*;
import org.memoriadb.core.*;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.ReflectionUtil;

public class ClassModeQueryStrategy {

  private static Method findMethod(Class<?> klazz) {
    for (Method method : klazz.getMethods()) {
      if (!method.getName().equals("accept")) continue;
      if (method.getParameterTypes().length != 2) continue;
      return method;
    }
    throw new MemoriaException("assumed to be unreachable");
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> query(IObjectRepository objectRepository, Class<T> clazz) {
    return internalQuery(objectRepository, clazz, new IFilter<T>() {

      @Override
      public boolean accept(T object, IFilterControl control) {
        return true;
      }
      
    });
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> query(IObjectRepository objectRepository, IFilter<T> filter) {

    Class<?> clazz = getFilterClass(filter);
    
    return internalQuery(objectRepository, clazz, filter);
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> query(IObjectRepository objectRepository, String clazz) {
    return (List<T>) query(objectRepository, (Class<Object>) ReflectionUtil.getClass(clazz));
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> query(IObjectRepository objectRepository, String clazz, IFilter<Object> filter) {
    return (List<T>) internalQuery(objectRepository, ReflectionUtil.getClass(clazz), filter);
  }

  private <T> Class<?> getFilterClass(IFilter<T> filter) {
    Method method = findMethod(filter.getClass());
    Class<?> clazz = method.getParameterTypes()[0];
    return clazz;
  }

  @SuppressWarnings("unchecked")
  private <T> List<T> internalQuery(IObjectRepository objectRepository, Class<?> clazz, IFilter<T> filter) {
    FilterControl control = new FilterControl();

    for (IObjectInfo info : objectRepository.getAllObjectInfos()) {
      if (!clazz.isInstance(info.getObject())) continue;

      T t = (T) info.getObject();
      if (filter.accept(t, control)) control.add(t);
      
      if(control.isAbort()) break;
      
    }
    return (List<T>) control.getResult();
  }

}
