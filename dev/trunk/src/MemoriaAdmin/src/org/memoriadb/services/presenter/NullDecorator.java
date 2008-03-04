package org.memoriadb.services.presenter;

import java.util.Collection;

import javax.swing.table.DefaultTableModel;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.IDataObject;

public class NullDecorator implements ITableModelDecorator {

  @Override
  public void addColumn(DefaultTableModel model, IMemoriaClass memoriaClass) {}

  @Override
  public void addRow(IDataObject dataObject, IMemoriaClass memoriaClass, Collection<Object> rowData) {}

}
