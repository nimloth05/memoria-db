package org.memoriadb.services.presenter;

import java.util.*;

import javax.swing.RowSorter;
import javax.swing.table.*;

import org.memoriadb.IDataStore;
import org.memoriadb.core.IObjectInfo;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.id.loong.LongId;

public class TableModel extends AbstractTableModel {
  
  private final List<String> fColumnNames = new ArrayList<String>();
  
  private final List<IDataObject> fQuery;
  private final ITableModelDecorator fDecorator;
  private final IDataStore fStore;
  
  public static TableModel create(List<IDataObject> query, ITableModelDecorator decorator, IDataStore store) {
    TableModel model = new TableModel(query, decorator, store);
    
    model.addColumn("ObjectId");
    model.addColumn("Revision");
    model.addColumn("Class ObjectId");
    decorator.addColumn(model);
    
    return model;
  }

  private TableModel(List<IDataObject> queryResult, ITableModelDecorator decorator, IDataStore store) {
    fQuery = queryResult;
    fDecorator = decorator;
    fStore = store;
  }
  
  public void addColumn(String name) {
    fColumnNames.add(name);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (columnIndex <= 2) return Long.class;
    
    return super.getColumnClass(columnIndex);
  }
  
  @Override
  public int getColumnCount() {
    return fColumnNames.size();
  }
  
  @Override
  public String getColumnName(int column) {
    return fColumnNames.get(column);
  }
  
  @Override
  public int getRowCount() {
    return fQuery.size();
  }

  public RowSorter<? extends TableModel> getRowSorter() {
    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this);
    
    Comparator<IObjectId> comparator = new Comparator<IObjectId>() {

      @Override
      public int compare(IObjectId o1, IObjectId o2) {
        if (o1 instanceof LongId && o2 instanceof LongId) {
          LongId id1 = (LongId) o1;
          LongId id2 = (LongId)o2;
          return id1.getLong() > id2.getLong() ? 1 : (id1.getLong() < id2.getLong() ? -1 : 0);
        }
        return o1.toString().compareTo(o2.toString());
      }

      
    };
    
    sorter.setComparator(0, comparator);
    sorter.setComparator(2, comparator);
    
    return sorter;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    IDataObject object = fQuery.get(rowIndex);
    
    if (columnIndex > 2) {
      return fDecorator.getValue(object, fColumnNames.get(columnIndex));
    }
    
    IObjectInfo objectInfo = fStore.getObjectInfo(object);
    if (columnIndex == 0) {
      return objectInfo.getId();
    }
    
    if (columnIndex == 1) {
      return objectInfo.getRevision();
    }
    
    if (columnIndex == 2) {
      return object.getMemoriaClassId();
    }
    
    throw new IllegalArgumentException("Unknown column, index: " + columnIndex);
  }

}