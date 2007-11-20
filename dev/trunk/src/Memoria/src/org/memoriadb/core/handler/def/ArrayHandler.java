package org.memoriadb.core.handler.def;

import java.io.*;
import java.lang.reflect.Array;

import org.memoriadb.core.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.load.binder.BindArray;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.*;

public class ArrayHandler implements ISerializeHandler {

  @Override
  public void checkCanInstantiateObject(String className, IDefaultInstantiator defaultInstantiator) {}

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws IOException {
    int dimension = input.readInt();
    int length = input.readInt();
    Type type = Type.values()[input.readByte()];
    
    Object array = instantiateArray(input, context, dimension, length, type);
    
    if(dimension == 1) {
      readObjects(input, context, array, type);
    }
    else {
      // multidimensional array, read components, which are also arrays
      readObjects(input, context, array, Type.typeClass);
    }
    
    return array;
  }

  @Override
  public String getClassName() {
    return Array.class.getName();
  }

  @Override
  public void serialize(Object array, DataOutputStream output, ISerializeContext context) throws IOException {
    if (!array.getClass().isArray()) throw new MemoriaException("Object is not an array: " + array);

    TypeInfo typeInfo = ReflectionUtil.getTypeInfo(array.getClass());
    int arrayLength = Array.getLength(array);

    output.writeInt(typeInfo.getDimension());
    output.writeInt(arrayLength);
    output.writeByte(typeInfo.getComponentType().ordinal());
    
    // if componentType is a class, write MemoriaClassId
    if(typeInfo.getComponentType() == Type.typeClass) {
      context.getMemoriaClassId(typeInfo.getClassName()).writeTo(output);
    }
    
    if(typeInfo.getDimension() == 1) {
      // store content of the array, either primitives are objectIds
      storeArrayContent(context, output, typeInfo, array);
    }
    else {
      // if dimension is bigger than one, references to the nested arrays are stored
      writeObjects(context, output, array);
    }
  }

  public void superDeserialize(Object result, DataInputStream input, IReaderContext context) {
    throw new UnsupportedOperationException("To be implemented!");
  }

  public void superSerialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception {
    throw new UnsupportedOperationException("To be implemented!");
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    if (!obj.getClass().isArray()) throw new MemoriaException("array expected but was " + obj);

    int length = Array.getLength(obj);
    for (int i = 0; i < length; ++i) {
      Object object = Array.get(obj, i);
      if(object == null) continue;
      if (Type.typeClass != Type.getType(object)) continue;
      traversal.handle(object);
    }
  }

  private Object instantiateArray(DataInputStream input, IReaderContext context, int dimension, int length, Type type) throws IOException {
    Class<?> componentType = null;
    if(type == Type.typeClass){
      // read MemoriaClass of componentType
      IObjectId memoriaClassId = context.readObjectId(input);
      IMemoriaClassConfig memoriaClass = (IMemoriaClassConfig) context.getObjectById(memoriaClassId);
      componentType = ReflectionUtil.getClass(memoriaClass.getJavaClassName());
    }
    else {
      componentType = type.getClassLiteral();
    }
    
    // Only the first is used to set the size of the array.
    int[] dimensions = new int[dimension];
    dimensions[0] = length;
    
    return Array.newInstance(componentType, dimensions);
  }

  private void readObject(DataInputStream input, final IReaderContext context, final Object array, Type type, final int index) {
    type.readValue(input, context, new ITypeVisitor() {

      @Override
      public void visitClass(Type type, IObjectId objectId) {
        context.objectToBind(new BindArray(array, index, objectId));
      }

      @Override
      public void visitEnum(Type type, int enumOrdinal) {
        
      }

      @Override
      public void visitPrimitive(Type type, Object value) {
        Array.set(array, index, value);
      }
      
    });    
  }

  private void readObjects(DataInputStream input, IReaderContext context, Object array, Type type) {
    for(int i = 0; i < Array.getLength(array); ++i) {
      readObject(input, context, array, type, i);
    }
  }

  /**
   * Stores the content of an one-dimensional array, either primitives or references
   */
  private void storeArrayContent(ISerializeContext context, DataOutputStream output, TypeInfo typeInfo, Object array) throws IOException {
    if(typeInfo.getDimension() != 1) throw new MemoriaException("one dimensional array expected");
    
    if(typeInfo.getComponentType() == Type.typeClass){
      writeObjects(context, output, array);
    }
    else {
      for(int i = 0; i < Array.getLength(array); ++i){
        typeInfo.getComponentType().writeValue(output, Array.get(array, i), context);
      }
    }
  }

  /**
   *  Writes the id of the given obj to the given stream or NullReference, if the given obj is null. 
   */
  private void writeId(ISerializeContext context, DataOutputStream output, Object obj) throws IOException {
    if(obj == null){
      context.getNullReference().writeTo(output);
    }
    else {
      context.getObjectId(obj).writeTo(output);
    }
  }

  private void writeObjects(ISerializeContext context, DataOutputStream output, Object array) throws IOException {
    for(int i = 0; i < Array.getLength(array); ++i){
      writeId(context, output, Array.get(array, i));
    }
  }

}
