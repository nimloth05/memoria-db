package org.memoriadb.services.presenter;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.handler.field.FieldbasedObjectHandler;
import org.memoriadb.handler.field.IFieldbasedObject;
import org.memoriadb.handler.field.MemoriaField;

import javax.swing.*;

public class FieldbasedObjectRenderer implements IClassRenderer {

  @Override
  public JComponent createControl() {
    return new JLabel("Field-Based Object Renderer");
  }

  @Override
  public ITableModelDecorator getTableModelDecorator(final IMemoriaClass memoriaClass) {
    return new ITableModelDecorator() {

      @Override
      public void addColumn(TableModel model) {
        FieldbasedObjectHandler handler = (FieldbasedObjectHandler) memoriaClass.getHandler();

        for(MemoriaField field: handler.getFields()) {
          model.addColumn(field.getName());
        }        
      }

      @Override
      public Object getValue(IDataObject object, String name) {
        return ((IFieldbasedObject)object).get(name);
      }
      
    };
  }

}
