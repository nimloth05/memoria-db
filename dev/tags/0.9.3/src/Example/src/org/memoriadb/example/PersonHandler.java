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

package org.memoriadb.example;

import java.io.*;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.util.io.IDataInput;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.handler.*;
import org.memoriadb.handler.field.*;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;
import org.memoriadb.example.classes.Person;

public class PersonHandler implements IHandler {
  
  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {}

  @Override
  public Object deserialize(IDataInput input, IReaderContext context, IObjectId typeId) throws Exception {
    IFieldbasedObject object = creeateObject(context, typeId);
    object.set(Person.NAME_FIELD, input.readUTF());
    return object.getObject();
  }

  @Override
  public String getClassName() {
    return Person.class.getName();
  }

  @Override
  public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
    IFieldbasedObject dataObject = createDataObject(obj);
    output.writeUTF(dataObject.get(Person.NAME_FIELD).toString());
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    
  }

  private IFieldbasedObject createDataObject(Object obj) {
    if (obj instanceof IDataObject) return (IFieldbasedObject) obj;
    return new FieldbasedObject(obj);
  }

  private IFieldbasedObject creeateObject(IReaderContext context, IObjectId typeId) {
    if (context.isInDataMode()) {
      return new FieldbasedDataObject(typeId);
    }
    return new FieldbasedObject(context.getDefaultInstantiator().newInstance(getClassName()));
  }

}
