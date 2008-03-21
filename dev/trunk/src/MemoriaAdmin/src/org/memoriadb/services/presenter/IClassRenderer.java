package org.memoriadb.services.presenter;

import javax.swing.JComponent;

import org.memoriadb.core.meta.IMemoriaClass;

public interface IClassRenderer {
  
  public JComponent createControl();

  public ITableModelDecorator getTableModelDecorator(IMemoriaClass memoriaClass);

}
