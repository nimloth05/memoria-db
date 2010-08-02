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

import org.memoriadb.IDataStore;
import org.memoriadb.handler.field.IFieldbasedObject;

import java.util.ArrayList;
import java.util.List;

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
