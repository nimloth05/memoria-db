package org.memoriadb.services.presenter;

import java.util.Collection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.handler.field.*;

public class FieldbasedObjectRenderer implements IClassRenderer {

  @Override
  public JComponent createControl() {
    return new JLabel("Field-Based Object Renderer");
  }

  @Override
  public ITableModelDecorator getTableModelDecorator() {
    return new ITableModelDecorator() {

      @Override
      public void addColumn(DefaultTableModel model, IMemoriaClass memoriaClass) {
        FieldbasedMemoriaClass fieldClass = (FieldbasedMemoriaClass) memoriaClass;
        
        for(MemoriaField field: fieldClass.getFields()) {
          model.addColumn(field.getName());
        }        
      }

      @Override
      public void addRow(IDataObject dataObject, IMemoriaClass memoriaClass, Collection<Object> rowData) {
        IFieldbasedObject fieldObject = (IFieldbasedObject) dataObject;
        FieldbasedMemoriaClass fieldClass = (FieldbasedMemoriaClass) memoriaClass;
        
        for(MemoriaField field: fieldClass.getFields()) {
          rowData.add(fieldObject.get(field.getName()));
        }
      }
      
    };
  }

}
