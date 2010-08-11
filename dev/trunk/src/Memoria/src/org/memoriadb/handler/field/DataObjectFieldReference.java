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

package org.memoriadb.handler.field;

import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.handler.IBindable;
import org.memoriadb.id.IObjectId;


public class DataObjectFieldReference implements IBindable {
  
  private final String fFieldName;
  private final IFieldbasedObject fSource;
  private final IObjectId fTargetObjectId;

  public DataObjectFieldReference(IFieldbasedObject source, String fieldName, IObjectId targetObjectId){
    fSource = source;
    fFieldName = fieldName;
    fTargetObjectId = targetObjectId;
  }

  @Override
  public void bind(IReaderContext context) throws Exception {
    fSource.set(fFieldName, context.getObject(fTargetObjectId));
  }
  
}
