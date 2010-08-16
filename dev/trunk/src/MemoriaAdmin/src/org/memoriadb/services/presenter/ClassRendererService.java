/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.services.presenter;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.IHandler;
import org.memoriadb.handler.field.FieldbasedObjectHandler;

import javax.swing.*;

public class ClassRendererService implements IClassRendererService {
  
  @Override
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
