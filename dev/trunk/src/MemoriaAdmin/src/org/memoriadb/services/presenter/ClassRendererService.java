package org.memoriadb.services.presenter;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.IHandler;
import org.memoriadb.handler.field.FieldbasedObjectHandler;

import javax.swing.*;

public class ClassRendererService implements IClassRendererService {
  
  public IClassRenderer getRednerer(IMemoriaClass memoriaClass) {
    IHandler handler = memoriaClass.getHandler();
    if (handler instanceof FieldbasedObjectHandler) {
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
