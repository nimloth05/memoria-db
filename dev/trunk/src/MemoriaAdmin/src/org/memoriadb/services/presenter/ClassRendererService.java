package org.memoriadb.services.presenter;

import javax.swing.JComponent;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.field.FieldbasedMemoriaClass;

public class ClassRendererService implements IClassRendererService {
  
  public IClassRenderer getRednerer(IMemoriaClass memoriaClass) {
    if (memoriaClass instanceof FieldbasedMemoriaClass) {
      return new FieldbasedObjectRenderer(); 
    }
    
    return new IClassRenderer() {

      @Override
      public JComponent createControl() {
        return null;
      }

      @Override
      public ITableModelDecorator getTableModelDecorator(IMemoriaClass memoriaClass) {
        return new NullDecorator();
      }
    };
  }
  
}
