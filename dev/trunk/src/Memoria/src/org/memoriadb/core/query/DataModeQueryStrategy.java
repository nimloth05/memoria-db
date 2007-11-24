package org.memoriadb.core.query;

import java.util.*;

import org.memoriadb.IFilter;
import org.memoriadb.core.*;
import org.memoriadb.core.meta.IMemoriaClass;

public class DataModeQueryStrategy implements IQueryStrategy {

  @SuppressWarnings("unchecked")
  @Override
  public <T> List<T> getAll(IObjectRepository objectRepository, Class<T> clazz) {
    return (List<T>)getAll(objectRepository, clazz.getName());
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> List<T> getAll(IObjectRepository objectRepository, Class<T> clazz, IFilter<T> filter) {
    return (List<T>)getAll(objectRepository, clazz.getName(), (IFilter<Object>)filter);
  }

  @Override
  public List<Object> getAll(IObjectRepository objectRepository, String clazz) {
    List<Object> result = new ArrayList<Object>();
    for (IObjectInfo objectInfo : objectRepository.getAllObjectInfos()) {
      IMemoriaClass memoriaClass = (IMemoriaClass) objectRepository.getObject(objectInfo.getMemoriaClassId());
      if (memoriaClass.isTypeFor(clazz)) result.add(objectInfo.getObject());
    }
    return result;
  }

  @Override
  public List<Object> getAll(IObjectRepository objectRepository, String clazz, IFilter<Object> filter) {
    List<Object> result = new ArrayList<Object>();

    for (IObjectInfo objectInfo : objectRepository.getAllObjectInfos()) {
      IMemoriaClass memoriaClass = (IMemoriaClass) objectRepository.getObject(objectInfo.getMemoriaClassId());

      if (memoriaClass.isTypeFor(clazz)) {
        if (filter.accept(objectInfo.getObject())) result.add(objectInfo.getObject());
      }
    }
    return result;
  }
}
