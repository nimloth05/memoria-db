package org.memoriadb.services.presenter;

import java.util.Collection;


import org.memoriadb.handler.IDataObject;

public class NullDecorator implements ITableModelDecorator {

  @Override
  public void addColumn(TableModel model) {}

  @Override
  public void addRow(IDataObject dataObject, Collection<Object> rowData) {}

}
