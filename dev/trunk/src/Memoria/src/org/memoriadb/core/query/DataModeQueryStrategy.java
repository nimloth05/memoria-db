package org.memoriadb.core.query;

import org.memoriadb.IFilter;
import org.memoriadb.IFilterControl;
import org.memoriadb.core.IObjectInfo;
import org.memoriadb.core.IObjectRepository;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.IDataObject;

import java.util.List;

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
    FilterControl<T> control = new FilterControl<T>();

    for (IObjectInfo objectInfo : objectRepository.getAllObjectInfo()) {
      IMemoriaClass memoriaClass = (IMemoriaClass) objectRepository.getObject(objectInfo.getMemoriaClassId());

      if (!memoriaClass.isTypeFor(clazz)) continue;
      T currentObject = (T)objectInfo.getObject();
      if (filter.accept(currentObject, control)) control.add(currentObject);
      if(control.isAbort()) break;
    }

    return control.getResult();
  }
}
