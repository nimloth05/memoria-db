package org.memoriadb.services.presenter;

import org.memoriadb.core.meta.IMemoriaClass;

public class ClassRendererService implements IClassRendererService {
  
  public IClassRenderer getRednerer(IMemoriaClass memoriaClass) {
    return new FieldbasedObjectRenderer();
  }
  
}
