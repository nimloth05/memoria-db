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

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.handler.field.FieldbasedObjectHandler;
import org.memoriadb.handler.field.IFieldbasedObject;
import org.memoriadb.handler.field.MemoriaField;

import javax.swing.*;

public class FieldbasedObjectRenderer implements IClassRenderer {

  @Override
  public JComponent createControl() {
    return new JLabel("Field-Based Object Renderer");
  }

  @Override
  public ITableModelDecorator getTableModelDecorator(final IMemoriaClass memoriaClass) {
    return new ITableModelDecorator() {

      @Override
      public void addColumn(TableModel model) {
        FieldbasedObjectHandler handler = (FieldbasedObjectHandler) memoriaClass.getHandler();

        for(MemoriaField field: handler.getFields()) {
          model.addColumn(field.getName());
        }        
      }

      @Override
      public Object getValue(IDataObject object, String name) {
        return ((IFieldbasedObject)object).get(name);
      }
      
    };
  }

}
