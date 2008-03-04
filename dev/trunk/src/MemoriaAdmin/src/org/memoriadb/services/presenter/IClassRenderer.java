package org.memoriadb.services.presenter;

import javax.swing.JComponent;

public interface IClassRenderer {
  
  public JComponent createControl();

  public ITableModelDecorator getTableModelDecorator();

}
