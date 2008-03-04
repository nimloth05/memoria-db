package org.memoriadb.services.presenter;

import java.util.Collection;

import javax.swing.table.DefaultTableModel;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.IDataObject;

public interface ITableModelDecorator {
  
  public void addColumn(DefaultTableModel model, IMemoriaClass memoriaClass);
  
  public void addRow(IDataObject dataObject, IMemoriaClass memoriaClass, Collection<Object> rowData);

}
