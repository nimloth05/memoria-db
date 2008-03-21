package org.memoriadb.ui.moodel;

import javax.swing.table.TableModel;

public interface IQueryListener {
  
  public void executed(org.memoriadb.services.presenter.TableModel model);

}
