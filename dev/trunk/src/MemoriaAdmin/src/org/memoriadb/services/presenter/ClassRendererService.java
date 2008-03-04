package org.memoriadb.services.presenter;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.handler.field.FieldbasedMemoriaClass;

public class ClassRendererService implements IClassRendererService {
  
  public IClassRenderer getRednerer(IMemoriaClass memoriaClass) {
    if (memoriaClass instanceof FieldbasedMemoriaClass) {
      return new FieldbasedObjectRenderer(); 
    }
    return new IClassRenderer() {

      @Override
      public void addColumnsToTable(DefaultTableModel model, IMemoriaClass memoriaClass) {
        // TODO Auto-generated method stub
        
      }

      @Override
      public void addToTableRow(IDataObject dataObject, IMemoriaClass memoriaClass, List<Object> rowData) {
        
      }

      @Override
      public JComponent createControl() {
        return null;
      }
    };
  }
  
}
