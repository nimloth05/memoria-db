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

package org.memoriadb.handler.array;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.core.meta.ITypeVisitor;
import org.memoriadb.core.meta.Type;
import org.memoriadb.core.util.ArrayTypeInfo;
import org.memoriadb.handler.IHandler;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Array;

public class ArrayHandler implements IHandler {

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {}

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws Exception {
    
    IArray array = ArrayTypeInfo.readTypeInfo(input, context);
    ArrayTypeInfo arrayTypeInfo = array.getTypeInfo();

    if (arrayTypeInfo.getDimension() == 1) {
      readContent(input, context, arrayTypeInfo.getComponentType(), array);
    }
    else {
      // multidimensional array, read components, which are also arrays
      readObjects(input, context, array);
    }

    return array.getResult();
  }

  @Override
  public String getClassName() {
    return Array.class.getName();
  }

  @Override
  public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
    IArray array = getArray(obj);

    ArrayTypeInfo arrayTypeInfo = array.getTypeInfo();

    arrayTypeInfo.writeTypeInfo(array.length(), output, context);

    if (arrayTypeInfo.getDimension() == 1) {
      // store content of the array, either primitives are objectIds
      storeArrayContent(context, output, arrayTypeInfo, array);
    }
    else {
      // if dimension is bigger than one, references to the nested arrays are stored
      writeObjects(context, output, array);
    }
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    IArray array = getArray(obj);
    
    //We must not traverse primitives like ints etc.
    if (array.getTypeInfo().getDimension() == 1 && array.getTypeInfo().getComponentType().isPrimitive()) return;

    int length = array.length();
    for (int i = 0; i < length; ++i) {
      Object object = array.get(i);
      if (object == null) continue;
      if (Type.isPrimitive(object)) continue;

      traversal.handle(object);
    }
  }

  private IArray getArray(Object obj) {
    if (obj instanceof IArray) { return (IArray) obj; }

    return new ObjectArray(obj);
  }

  private void readContent(DataInputStream input, IReaderContext context, Type componentType, IArray array) throws Exception {
    if (componentType.isPrimitive()) {
      readPrimitives(input, context, array, componentType);
    }
    else {
      readObjects(input, context, array);
    }
  }

  private void readObject(DataInputStream input, final IReaderContext context, final IArray array, final int index) {
    Type.readValueWithType(input, context, new ITypeVisitor() {

      @Override
      public void visitClass(Type type, IObjectId objectId) {
        context.addGenOneBinding(new BindArray(array, index, objectId));
      }

      @Override
      public void visitNull() {
        array.set(index, null);        
      }

      @Override
      public void visitPrimitive(Type type, Object value) {
        array.set(index, value);
      }

      @Override
      public void visitValueObject(Object value) {
        array.set(index, value);
      }

    });
  }

  private void readObjects(DataInputStream input, IReaderContext context, IArray array) {
    for (int i = 0; i < array.length(); ++i) {
      readObject(input, context, array, i);
    }
  }

  private void readPrimitive(DataInputStream input, IReaderContext context, final IArray array, final int index, Type componentType) throws Exception {
    componentType.readValue(input, context, new ITypeVisitor() {

      @Override
      public void visitClass(Type type, IObjectId objectId) {
        throw new MemoriaException("primitive expected");
      }

      @Override
      public void visitNull() {
        array.set(index, null);        
      }

      @Override
      public void visitPrimitive(Type type, Object value) {
        array.set(index, value);
      }

      @Override
      public void visitValueObject(Object value) {
        array.set(index, value);
      }

    });
  }

  private void readPrimitives(DataInputStream input, IReaderContext context, IArray array, Type componentType) throws Exception {
    for (int i = 0; i < array.length(); ++i) {
      readPrimitive(input, context, array, i, componentType);
    }
  }

  /**
   * Stores the content of an one-dimensional array, either primitives or references
   * @throws IOException 
   */
  private void storeArrayContent(IWriterContext context, DataOutput output, ArrayTypeInfo componentTypeInfo, IArray array) throws Exception {
    if (componentTypeInfo.getDimension() != 1) throw new MemoriaException("one dimensional array expected");

    if (componentTypeInfo.getComponentType().isPrimitive()) {
      writePrimitives(context, output, componentTypeInfo, array);
    }
    else {
      writeObjects(context, output, array);
    }
  }

  /**
   * Writes the id of the given obj to the given stream or NullReference, if the given obj is null.
   * @throws IOException 
   */
  private void writeObject(IWriterContext context, DataOutput output, Object obj) throws Exception {
    Type.writeValueWithType(output, obj, context);
  }

  private void writeObjects(IWriterContext context, DataOutput output, IArray array) throws Exception {
    for (int i = 0; i < array.length(); ++i) {
      writeObject(context, output, array.get(i));
    }
  }

  private void writePrimitives(IWriterContext context, DataOutput output, ArrayTypeInfo componentTypeInfo, IArray array) throws Exception {
    for (int i = 0; i < array.length(); ++i) {
      componentTypeInfo.getComponentType().writeValue(output, array.get(i), context);
    }
  }

}
