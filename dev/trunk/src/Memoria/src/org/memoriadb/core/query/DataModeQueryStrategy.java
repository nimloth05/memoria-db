package org.memoriadb.core.query;

import java.util.List;

import org.memoriadb.*;
import org.memoriadb.core.*;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.IDataObject;

public class DataModeQueryStrategy {

  public <T extends IDataObject> List<T>  query(IObjectRepository objectRepository, String clazz) {
    return query(objectRepository, clazz, new IFilter<T>(){
 
      @Override
      public boolean accept(T object, IFilterControl control) {
        return true;
      }
      
    });
  }

  @SuppressWarnings("unchecked")
  public <T extends IDataObject> List<T> query(IObjectRepository objectRepository, String clazz, IFilter<T> filter) {
    FilterControl control = new FilterControl();

    for (IObjectInfo objectInfo : objectRepository.getAllObjectInfos()) {
      IMemoriaClass memoriaClass = (IMemoriaClass) objectRepository.getObject(objectInfo.getMemoriaClassId());

      if (!memoriaClass.isTypeFor(clazz)) continue;
      if (filter.accept((T)objectInfo.getObject(), control)) control.add(objectInfo.getObject());
      if(control.isAbort()) break;
    }

    return (List<T>) control.getResult();
  }
}
