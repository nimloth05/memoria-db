package org.memoriadb.handler.array;

import java.io.*;
import java.lang.reflect.Array;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.util.ArrayTypeInfo;
import org.memoriadb.handler.IHandler;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

public class ArrayHandler implements IHandler {

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {}

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws IOException {
    int dimension = input.readInt();
    int length = input.readInt();
    Type componentType = Type.values()[input.readByte()];

    IArray array = instantiateArray(input, context, dimension, length, componentType);

    if (dimension == 1) {
      if (componentType.isPrimitive()) {
        readPrimitives(input, context, array, componentType);
      }
      else {
        readObjects(input, context, array);
      }
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
  public void serialize(Object obj, DataOutput output, ISerializeContext context) throws IOException {
    IArray array = getArray(obj);

    ArrayTypeInfo componentTypeInfo = array.getComponentTypeInfo();

    output.writeInt(componentTypeInfo.getDimension());
    output.writeInt(array.length());
    output.writeByte(componentTypeInfo.getComponentType().ordinal());

    // if componentType is a class, write MemoriaClassId
    if (componentTypeInfo.getComponentType() == Type.typeClass) {
      context.getMemoriaClassId(componentTypeInfo.getClassName()).writeTo(output);
    }

    if (componentTypeInfo.getDimension() == 1) {
      // store content of the array, either primitives are objectIds
      storeArrayContent(context, output, componentTypeInfo, array);
    }
    else {
      // if dimension is bigger than one, references to the nested arrays are stored
      writeObjects(context, output, array);
    }
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    IArray array = getArray(obj);

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

  private IArray instantiateArray(DataInputStream input, IReaderContext context, int dimension, int length, Type componentType)
      throws IOException {

    if (componentType.isPrimitive()) {
      // name is given only for debug-reasons
      ArrayTypeInfo arrayTypeInfo = new ArrayTypeInfo(componentType, dimension, componentType.name());

      return instantiateArray(context, length, arrayTypeInfo);
    }

    // read MemoriaClass of componentType
    IObjectId memoriaClassId = context.readObjectId(input);
    IMemoriaClassConfig memoriaClass = (IMemoriaClassConfig) context.getExistingObject(memoriaClassId);

    ArrayTypeInfo arrayTypeInfo = new ArrayTypeInfo(componentType, dimension, memoriaClass.getJavaClassName());

    return instantiateArray(context, length, arrayTypeInfo);
  }

  private IArray instantiateArray(IReaderContext context, int length, ArrayTypeInfo arrayTypeInfo) {
    if (context.isInDataMode()) return new DataArray(context.getArrayMemoriaClass(), arrayTypeInfo, length);
    return new ObjectArray(context.getArrayMemoriaClass(), arrayTypeInfo, length);
  }

  private void readObject(DataInputStream input, final IReaderContext context, final IArray array, final int index) {
    Type.readValueWithType(input, context, new ITypeVisitor() {

      @Override
      public void visitClass(Type type, IObjectId objectId) {
        context.objectToBind(new BindArray(array, index, objectId));
      }

      @Override
      public void visitNull() {
        array.set(index, null);        
      }

      @Override
      public void visitPrimitive(Type type, Object value) {
        array.set(index, value);
      }

    });
  }

  private void readObjects(DataInputStream input, IReaderContext context, IArray array) {
    for (int i = 0; i < array.length(); ++i) {
      readObject(input, context, array, i);
    }
  }

  private void readPrimitive(DataInputStream input, IReaderContext context, final IArray array, final int index, Type componentType) throws IOException {
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

    });
  }

  private void readPrimitives(DataInputStream input, IReaderContext context, IArray array, Type componentType) throws IOException {
    for (int i = 0; i < array.length(); ++i) {
      readPrimitive(input, context, array, i, componentType);
    }
  }

  /**
   * Stores the content of an one-dimensional array, either primitives or references
   * @throws IOException 
   */
  private void storeArrayContent(ISerializeContext context, DataOutput output, ArrayTypeInfo componentTypeInfo, IArray array) throws IOException {
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
  private void writeObject(ISerializeContext context, DataOutput output, Object obj) throws IOException {
    Type.writeValueWithType(output, obj, context);
  }

  private void writeObjects(ISerializeContext context, DataOutput output, IArray array) throws IOException {
    for (int i = 0; i < array.length(); ++i) {
      writeObject(context, output, array.get(i));
    }
  }

  private void writePrimitives(ISerializeContext context, DataOutput output, ArrayTypeInfo componentTypeInfo, IArray array) throws IOException {
    for (int i = 0; i < array.length(); ++i) {
      componentTypeInfo.getComponentType().writeValue(output, array.get(i), context);
    }
  }

}
