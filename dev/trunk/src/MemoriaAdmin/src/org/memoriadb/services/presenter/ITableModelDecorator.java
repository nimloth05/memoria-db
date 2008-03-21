package org.memoriadb.services.presenter;

import org.memoriadb.handler.IDataObject;

public interface ITableModelDecorator {
  
  public void addColumn(TableModel model);
  
  public Object getValue(IDataObject object, String name);

}
