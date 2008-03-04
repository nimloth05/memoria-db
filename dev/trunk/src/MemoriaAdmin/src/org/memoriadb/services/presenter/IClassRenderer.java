package org.memoriadb.services.presenter;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.IDataObject;

public interface IClassRenderer {
  
  public void addColumnsToTable(DefaultTableModel model, IMemoriaClass memoriaClass);

  public void addToTableRow(IDataObject dataObject, IMemoriaClass memoriaClass, List<Object> rowData);

  public JComponent createControl();

}
