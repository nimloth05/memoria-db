package org.memoriadb.core.query;

import java.util.*;

import org.memoriadb.IFilter;
import org.memoriadb.core.*;
import org.memoriadb.util.ReflectionUtil;

public class ClassModeQueryStrategy implements IQueryStrategy {

  @SuppressWarnings("unchecked")
  @Override
  public <T> List<T> getAll(IObjectRepo objectRepo, Class<T> clazz) {
    List<T> result = new ArrayList<T>(10);
    for(IObjectInfo info: objectRepo.getAllObjectInfos()) {
      if (clazz.isInstance(info.getObject())) result.add((T)info.getObject());
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> List<T> getAll(IObjectRepo objectRepo, Class<T> clazz, IFilter<T> filter) {
    List<T> result = new ArrayList<T>(10);
    for(IObjectInfo info: objectRepo.getAllObjectInfos()) {
      if (clazz.isInstance(info.getObject())) {
        T t = (T) info.getObject();
        if (filter.accept(t)) result.add(t);
      }
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Object> getAll(IObjectRepo objectRepo, String clazz) {
    return getAll(objectRepo, (Class<Object>)ReflectionUtil.getClass(clazz));
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Object> getAll(IObjectRepo objectRepo, String clazz, IFilter<Object> filter) {
    return getAll(objectRepo, (Class<Object>)ReflectionUtil.getClass(clazz), filter);
  }

}
