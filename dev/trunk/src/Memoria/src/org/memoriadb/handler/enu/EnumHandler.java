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

package org.memoriadb.handler.enu;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.util.io.IDataInput;
import org.memoriadb.handler.IHandler;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

import java.io.*;

public class EnumHandler implements IHandler {

  private final String fName;

  public EnumHandler(Class<?> javaClass) {
    this(javaClass.getName());
  }

  public EnumHandler(String name) {
    fName = name;
  }

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {}

  @Override
  public Object deserialize(IDataInput input, IReaderContext context, IObjectId typeId) throws Exception {
    IEnumObject enumObject = createEnumObject(context, typeId);
    enumObject.setName(input.readUTF());
    return enumObject.getObject((IMemoriaClass) context.getExistingObject(typeId));
  }

  @Override
  public String getClassName() {
    return fName;
  }

  @Override
  public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
    IEnumObject enumObject = createEnumObject(obj);
    output.writeUTF(enumObject.getName());
  }
  
  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {}

  private IEnumObject createEnumObject(IReaderContext context, IObjectId memoriaClassId) {
    if (context.isInDataMode()) return new EnumDataObject(memoriaClassId);
    return new EnumObject(memoriaClassId);
  }

  private IEnumObject createEnumObject(Object obj) {
    if (obj instanceof IEnumObject) return (IEnumObject) obj;
    return new EnumObject((Enum<?>) obj);
  }

}
