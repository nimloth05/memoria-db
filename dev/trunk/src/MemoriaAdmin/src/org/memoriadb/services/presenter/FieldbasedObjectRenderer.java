package org.memoriadb.services.presenter;

import javax.swing.*;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.handler.field.*;

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
        FieldbasedMemoriaClass fieldClass = (FieldbasedMemoriaClass) memoriaClass;
        
        for(MemoriaField field: fieldClass.getFields()) {
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
