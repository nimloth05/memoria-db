package org.memoriadb.services.presenter;

import org.memoriadb.handler.IDataObject;

public class NullDecorator implements ITableModelDecorator {

  @Override
  public void addColumn(TableModel model) {}

  @Override
  public Object getValue(IDataObject object, String name) {
    return null;
  }

  
}
