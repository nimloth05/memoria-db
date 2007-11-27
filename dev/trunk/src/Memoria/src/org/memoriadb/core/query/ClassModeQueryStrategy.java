package org.memoriadb.core.query;

import java.util.List;

import org.memoriadb.*;
import org.memoriadb.core.*;
import org.memoriadb.core.util.ReflectionUtil;

public class ClassModeQueryStrategy {

  @SuppressWarnings("unchecked")
  public <T> List<T> query(IObjectRepository objectRepository, Class<T> clazz) {
    return query(objectRepository, clazz, new IFilter<T>() {

      @Override
      public boolean accept(T object, IFilterControl control) {
        return true;
      }
      
    });
  }

  @SuppressWarnings("unchecked")
   public <FILTER, T extends FILTER> List<T> query(IObjectRepository objectRepository, Class<T> clazz, IFilter<FILTER> filter) {
    
    FilterControl control = new FilterControl();

    for (IObjectInfo info : objectRepository.getAllObjectInfos()) {
      if (!clazz.isInstance(info.getObject())) continue;

      T t = (T) info.getObject();
      if (filter.accept(t, control)) control.add(t);
      
      if(control.isAbort()) break;
      
    }
    return (List<T>) control.getResult();
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> query(IObjectRepository objectRepository, String clazz) {
    return (List<T>) query(objectRepository, (Class<Object>) ReflectionUtil.getClass(clazz));
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> query(IObjectRepository objectRepository, String clazz, IFilter<Object> filter) {
    return (List<T>) query(objectRepository, ReflectionUtil.getClass(clazz), filter);
  }

}
