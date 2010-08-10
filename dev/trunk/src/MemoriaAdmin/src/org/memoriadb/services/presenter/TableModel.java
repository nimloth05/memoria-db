/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.services.presenter;

import java.util.*;
import java.util.regex.PatternSyntaxException;

import javax.swing.RowSorter;
import javax.swing.table.*;

import org.memoriadb.IDataStore;
import org.memoriadb.core.IObjectInfo;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.IObjectId;

public class TableModel extends AbstractTableModel {
  
  private final List<String> fColumnNames = new ArrayList<String>();
  
  private final List<IDataObject> fQuery;
  private final ITableModelDecorator fDecorator;
  private final IDataStore fStore;

  private TableRowSorter<TableModel> fSorter;

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

  public void filter(String filterText) {
    RegexRowFilter rowFilter;
    
    try {
      rowFilter = new RegexRowFilter(filterText);
    } 
    catch (PatternSyntaxException e) {
      return;
    }
    
    fSorter.setRowFilter(rowFilter);
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
    fSorter = new TableRowSorter<TableModel>(this);
    Comparator<IObjectId> objectIdComparator = new ObjectIdComparator();
    
    fSorter.setComparator(0, objectIdComparator);
    fSorter.setComparator(2, objectIdComparator);
    
    return fSorter;
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
      return fStore.getRevision(objectInfo.getId());
    }
    
    if (columnIndex == 2) {
      return object.getMemoriaClassId();
    }
    
    throw new IllegalArgumentException("Unknown column, index: " + columnIndex);
  }

}
