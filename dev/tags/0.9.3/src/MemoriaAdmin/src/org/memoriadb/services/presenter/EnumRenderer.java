package org.memoriadb.services.presenter;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.field.FieldbasedObjectHandler;
import org.memoriadb.handler.field.MemoriaField;
import org.memoriadb.handler.field.IFieldbasedObject;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.handler.enu.EnumHandler;
import org.memoriadb.handler.enu.IEnumObject;

import javax.swing.*;

/**
 * @author Sandro
 */
public final class EnumRenderer implements IClassRenderer {

  @Override
  public JComponent createControl() {
    return new JLabel("WIP");
  }

  @Override
  public ITableModelDecorator getTableModelDecorator(final IMemoriaClass memoriaClass) {
    return new ITableModelDecorator() {

      @Override
      public void addColumn(TableModel model) {
        model.addColumn("Name");
      }

      @Override
      public Object getValue(IDataObject object, String name) {
        return ((IEnumObject)object).getName();
      }

    };
  }
}
