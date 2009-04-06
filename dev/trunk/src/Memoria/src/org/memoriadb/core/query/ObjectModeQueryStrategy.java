package org.memoriadb.core.query;

import java.util.List;

import org.memoriadb.*;
import org.memoriadb.core.IObjectRepository;
import org.memoriadb.core.util.ReflectionUtil;

public class ObjectModeQueryStrategy {

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
    FilterControl<T> control = new FilterControl<T>();

    for (Object object : objectRepository.getAllUserSpaceObjects()) {
      if (!clazz.isInstance(object)) continue;

      T currentObject = (T) object;
      if (filter.accept(currentObject, control)) control.add(currentObject);
      
      if(control.isAbort()) break;
      
    }
    return control.getResult();
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
