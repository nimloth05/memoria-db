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

package org.memoriadb.core.meta;

import java.io.*;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.SchemaException;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.core.util.io.IDataInput;
import org.memoriadb.handler.IHandler;
import org.memoriadb.handler.field.ClassInheritanceBinding;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

/**
 * @author Sandro
 */
public class HandlerClassHandler implements IHandler {

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {
    if (!MemoriaClass.class.getName().equals(className))
      throw new SchemaException("I am a handler for type " + MemoriaClass.class.getName() +" but I was called for " + className);
  }

  @Override
  public Object deserialize(IDataInput input, IReaderContext context, IObjectId typeId) throws IOException {
    String javaClassName = input.readUTF();
    String handlerName = input.readUTF();
    boolean hasValueObjectAnnotation = input.readBoolean();
    IObjectId superClassId = context.readObjectId(input);
    
    IHandler handler = instantiateHandler(handlerName, javaClassName);
    
    MemoriaClass memoriaClass = new MemoriaClass(handler, typeId, hasValueObjectAnnotation);
    
    if (!context.isRootClassId(superClassId)) context.addGenOneBinding(new ClassInheritanceBinding(memoriaClass, superClassId));
      
    return memoriaClass;
  }

  @Override
  public String getClassName() {
    return MemoriaClass.class.getName();
  }

  @Override
  public void serialize(Object obj, DataOutput output, IWriterContext context) throws IOException {
    MemoriaClass classObject = (MemoriaClass) obj;
    
    output.writeUTF(classObject.getJavaClassName());
    output.writeUTF(classObject.getHandlerName());
    output.writeBoolean(classObject.isValueObject());
    
    IObjectId superClassId = context.getRootClassId();
    if (classObject.getSuperClass() != null) {
      superClassId = context.getExistingtId(classObject.getSuperClass());
    }
    superClassId.writeTo(output);
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {}

  private IHandler instantiateHandler(String handlerName, String javaClassName) {
    return ReflectionUtil.createInstanceWithDefaultOrStringCtor(handlerName, javaClassName);
  }

}