package org.memoriadb.services.presenter;

import java.util.*;

import org.memoriadb.IDataStore;
import org.memoriadb.handler.field.IFieldbasedObject;

public class TableFormat implements ca.odell.glazedlists.gui.TableFormat<IFieldbasedObject> {
  
  private final List<String> fColumns = new ArrayList<String>();
  private final IDataStore fOpenStore;
  
  public TableFormat(IDataStore openStore) {
    fOpenStore = openStore;
    
    addDefaultColumns();
  }

  public void addColumn(String name) {
    fColumns.add(name);
  }

  @Override
  public int getColumnCount() {
    return fColumns.size();
  }
  
  @Override
  public String getColumnName(int column) {
    return fColumns.get(column);
  }

  @Override
  public Object getColumnValue(IFieldbasedObject baseObject, int column) {
    if (column == 0) {
      return fOpenStore.getObjectInfo(baseObject).getId();
    }
    if (column == 1) {
      return fOpenStore.getObjectInfo(baseObject).getRevision();
    }
    if (column == 2) {
      return baseObject.getMemoriaClassId();
    }
    
    return baseObject.get(fColumns.get(column));
  }

  private void addDefaultColumns() {
    addColumn("ObjectId");
    addColumn("Revision");
    addColumn("Class ObjectId");
  }

}
