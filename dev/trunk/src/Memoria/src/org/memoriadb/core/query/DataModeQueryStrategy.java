package org.memoriadb.core.query;

import java.util.*;

import org.memoriadb.IFilter;
import org.memoriadb.core.*;
import org.memoriadb.core.meta.IMemoriaClass;

public class DataModeQueryStrategy implements IQueryStrategy {

  @SuppressWarnings("unchecked")
  @Override
  public <T> List<T> getAll(IObjectRepo objectRepo, Class<T> clazz) {
    return (List<T>)getAll(objectRepo, clazz.getName());
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> List<T> getAll(IObjectRepo objectRepo, Class<T> clazz, IFilter<T> filter) {
    return (List<T>)getAll(objectRepo, clazz.getName(), (IFilter<Object>)filter);
  }

  @Override
  public List<Object> getAll(IObjectRepo objectRepo, String clazz) {
    List<Object> result = new ArrayList<Object>();
    for (IObjectInfo objectInfo : objectRepo.getAllObjectInfos()) {
      IMemoriaClass memoriaClass = (IMemoriaClass) objectRepo.getObject(objectInfo.getMemoriaClassId());
      if (memoriaClass.isTypeFor(clazz)) result.add(objectInfo.getObject());
    }
    return result;
  }

  @Override
  public List<Object> getAll(IObjectRepo objectRepo, String clazz, IFilter<Object> filter) {
    List<Object> result = new ArrayList<Object>();

    for (IObjectInfo objectInfo : objectRepo.getAllObjectInfos()) {
      IMemoriaClass memoriaClass = (IMemoriaClass) objectRepo.getObject(objectInfo.getMemoriaClassId());

      if (memoriaClass.isTypeFor(clazz)) {
        if (filter.accept(objectInfo.getObject())) result.add(objectInfo.getObject());
      }
    }
    return result;
  }
}
