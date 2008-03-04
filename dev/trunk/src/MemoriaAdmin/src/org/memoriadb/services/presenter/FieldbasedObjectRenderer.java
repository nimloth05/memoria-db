package org.memoriadb.services.presenter;

import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.handler.field.*;

public class FieldbasedObjectRenderer implements IClassRenderer {

  @Override
  public void addColumnsToTable(DefaultTableModel model, IMemoriaClass memoriaClass) {
    FieldbasedMemoriaClass fieldClass = (FieldbasedMemoriaClass) memoriaClass;
    
    for(MemoriaField field: fieldClass.getFields()) {
      model.addColumn(field.getName());
    }
  }

  @Override
  public void addToTableRow(IDataObject dataObject, IMemoriaClass memoriaClass, List<Object> rowData) {
    IFieldbasedObject fieldObject = (IFieldbasedObject) dataObject;
    FieldbasedMemoriaClass fieldClass = (FieldbasedMemoriaClass) memoriaClass;
    
    for(MemoriaField field: fieldClass.getFields()) {
      rowData.add(fieldObject.get(field.getName()));
    }
  }

  @Override
  public JComponent createControl() {
    return new JLabel("Field-Based Object Renderer");
  }

}
