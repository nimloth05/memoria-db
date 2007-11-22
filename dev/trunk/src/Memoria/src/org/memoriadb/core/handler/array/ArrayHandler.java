package org.memoriadb.core.handler.array;

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
    Type componentType = Type.values()[input.readByte()];
    
    IArray array = instantiateArray(input, context, dimension, length, componentType);
    
    if(dimension == 1) {
      if(componentType.isPrimitive()) { 
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
    
    return array;
  }

  @Override
  public String getClassName() {
    return Array.class.getName();
  }

  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws IOException {
    IArray array = getArray(obj);

    TypeInfo componentTypeInfo = ReflectionUtil.getComponentTypeInfo(array.getClass());
    int arrayLength = Array.getLength(array);

    output.writeInt(componentTypeInfo.getDimension());
    output.writeInt(arrayLength);
    output.writeByte(componentTypeInfo.getComponentType().ordinal());
    
    // if componentType is a class, write MemoriaClassId
    if(componentTypeInfo.getComponentType() == Type.typeClass) {
      context.getMemoriaClassId(componentTypeInfo.getClassName()).writeTo(output);
    }
    
    if(componentTypeInfo.getDimension() == 1) {
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
      if(object == null) continue;
      if(Type.isPrimitive(object)) continue;
       
      traversal.handle(object);
    }
  }

  private IArray getArray(Object obj) {
    if(obj instanceof IArray){
      return (IArray) obj;
    }

    return new ObjectArray(obj);
  }

  private IArray instantiateArray(DataInputStream input, IReaderContext context, int dimension, int length, Type componentType) throws IOException {
    String componentClassName = null;
    if(componentType == Type.typeClass){
      // read MemoriaClass of componentType
      IObjectId memoriaClassId = context.readObjectId(input);
      IMemoriaClassConfig memoriaClass = (IMemoriaClassConfig) context.getObjectById(memoriaClassId);
      componentClassName = memoriaClass.getJavaClassName();
      return new ObjectArray(memoriaClassId, memoriaClass, dimension, length);
    }
    else {
      componentClassName = componentType.getj;
    }
    
  }

  private void readObject(DataInputStream input, final IReaderContext context, final Object array, final int index) {
    Type.readValueWithType(input, context, new ITypeVisitor() {

      @Override
      public void visitClass(Type type, IObjectId objectId) {
        context.objectToBind(new BindArray(array, index, objectId));
      }

      @Override
      public void visitEnum(Type type, int enumOrdinal) {
        //FIXME muss noch implementiert werden
      }

      @Override
      public void visitPrimitive(Type type, Object value) {
        Array.set(array, index, value);
      }
      
    });    
  }

  private void readObjects(DataInputStream input, IReaderContext context, Object array) {
    for(int i = 0; i < Array.getLength(array); ++i) {
      readObject(input, context, array, i);
    }
  }

  private void readPrimitive(DataInputStream input, IReaderContext context, final IArray array, final int index, Type componentType) {
    componentType.readValue(input, context, new ITypeVisitor() {

      @Override
      public void visitClass(Type type, IObjectId objectId) {
        throw new MemoriaException("primitive expected");
      }

      @Override
      public void visitEnum(Type type, int enumOrdinal) {
        throw new MemoriaException("primitive expected");
      }

      @Override
      public void visitPrimitive(Type type, Object value) {
        array.set(index, value);        
      }
      
    });
  }

  private void readPrimitives(DataInputStream input, IReaderContext context, IArray array, Type componentType) {
    for(int i = 0; i < Array.getLength(array); ++i) {
      readPrimitive(input, context, array, i, componentType);
    }
  }

  /**
   * Stores the content of an one-dimensional array, either primitives or references
   */
  private void storeArrayContent(ISerializeContext context, DataOutputStream output, TypeInfo componentTypeInfo, IArray array) throws IOException {
    if(componentTypeInfo.getDimension() != 1) throw new MemoriaException("one dimensional array expected");
    
    if(componentTypeInfo.getComponentType().isPrimitive()){
      writePrimitives(context, output, componentTypeInfo, array);
    }
    else {
      writeObjects(context, output, array);
    }
  }

  /**
   *  Writes the id of the given obj to the given stream or NullReference, if the given obj is null. 
   */
  private void writeObject(ISerializeContext context, DataOutputStream output, Object obj) throws IOException {
    if(obj == null){
      Type.writeValueWithType(output, null, context, Type.typeClass);
      return;
    }
    
    Type.writeValueWithType(output, obj, context);
  }

  private void writeObjects(ISerializeContext context, DataOutputStream output, IArray array) throws IOException {
    for(int i = 0; i < array.length(); ++i){
      writeObject(context, output, array.get(i));
    }
  }

  private void writePrimitives(ISerializeContext context, DataOutputStream output, TypeInfo componentTypeInfo, IArray array) {
    for(int i = 0; i < Array.getLength(array); ++i){
      componentTypeInfo.getComponentType().writeValue(output, Array.get(array, i), context);
    }
  }

}
