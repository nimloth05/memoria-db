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

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.SchemaException;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.core.meta.MemoriaClass;
import org.memoriadb.core.meta.Type;
import org.memoriadb.handler.IHandler;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Handler which persists FieldBasedHandlers. This handler will always return a complete memoriaClass.
 */
public class FieldBasedHandlerHandler implements IHandler {

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {
    if (!getClassName().equals(className))
      throw new SchemaException("I am a handler for type " + getClassName() +" but I was called for " + className);
  }
  
  @Override
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws IOException {
    String className = input.readUTF();
    boolean hasValueObjectAnnotation = input.readBoolean();

    FieldbasedObjectHandler handler = new FieldbasedObjectHandler(className);
    MemoriaClass classObject = new MemoriaClass(handler, typeId, hasValueObjectAnnotation);

    IObjectId superClassId = context.readObjectId(input);
    if (!context.isRootClassId(superClassId)) context.addGenOneBinding(new ClassInheritanceBinding(classObject, superClassId)); 
    
    while (input.available() > 0) {
      int fieldId = input.readInt();
      String name = input.readUTF();
      int ordinal = input.readInt();
      boolean isWeakRef = input.readBoolean();
      MemoriaField metaField = new MemoriaField(fieldId, name, Type.values()[ordinal], isWeakRef);
      handler.addMetaField(metaField);
    }
    return classObject;
  }

  @Override
  public String getClassName() {
    return "ReflectionBasedHandlers";
  }

  @Override
  public void serialize(Object obj, DataOutput output, IWriterContext context) throws IOException {
    MemoriaClass classObject = (MemoriaClass) obj;
    FieldbasedObjectHandler handler = (FieldbasedObjectHandler) classObject.getHandler();
    
    output.writeUTF(handler.getClassName());
    output.writeBoolean(classObject.isValueObject());
    
    IObjectId superClassId = context.getRootClassId();
    if (classObject.getSuperClass() != null) superClassId = context.getExistingtId(classObject.getSuperClass());
    superClassId.writeTo(output);
    
    for(MemoriaField field: handler.getFields()) {
      output.writeInt(field.getId());
      output.writeUTF(field.getName());
      output.writeInt(field.getType().ordinal());
      output.writeBoolean(field.isWeakRef());
    }
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {}

}
