package org.memoriadb.core.handler.def;

import java.io.*;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.IReaderContext;

public class ArrayHandler implements ISerializeHandler {

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context, IObjectId typeId) throws IOException {
//    IObjectId compundTypeId = context.createFrom(input);
//    int dimensions = input.readInt();
//    
//    Class<?> componentType = getClassFromTypeInfo(compundTypeId, context);
//    int[] dimensionsArray = (int[]) Array.newInstance(int.class, dimensions);
//    dimensionsArray[0] = input.readInt();
//    Object array = java.lang.reflect.Array.newInstance(componentType, dimensionsArray);
//
//    TypeVisitorHelper<Object, Integer> visitor = new TypeVisitorHelper<Object, Integer>(context) {
//
//      @Override
//      public void visitClass(Type type, IObjectId objectId) {
//        fContext.objectToBind(new BindArray(fResult, fMember, objectId));
//      }
//
//      @Override
//      public void visitPrimitive(Type type, Object value) {
//        Array.set(fResult, fMember, value);
//      }
//    };
//    
//    visitor.setResult(array);
//    
//    for(int index = 0; input.available() > 0; ++index) {
//      visitor.setMember(index);
//      byte typeByte = input.readByte();
//      if (typeByte == Byte.MIN_VALUE) {
//        Array.set(array, index, deserialize(input, context));
//        continue;
//      }
//      Type.values()[typeByte].readValue(input, visitor, context);
//    }
//    
//    return array;
    throw new UnsupportedOperationException("To be implemented!");
  }

  @Override
  public void serialize(Object array, DataOutputStream output, ISerializeContext context) throws IOException {
//   if (!array.getClass().isArray()) throw new MemoriaException("Object is not an array: " + array);
//   
//   int dimension = 1;
//   Class<?> componentType = array.getClass().getComponentType();
//   while(componentType.isArray()) {
//     componentType = componentType.getComponentType();
//     ++dimension;
//   }
//   
//   IObjectId componentTypeId = componentTypeToId(context, componentType);
//   int arrayLength = Array.getLength(array);
//   
//   componentTypeId.writeTo(output);
//   output.writeInt(dimension);
//   output.writeInt(arrayLength);
//   
//   for(int i = 0; i < arrayLength; ++i) {
//     Object componentObject = Array.get(array, i);
//     if (componentObject.getClass().isArray()) {
//       output.write(Byte.MIN_VALUE);
//       serialize(componentObject, output, context);
//     } 
//     else {
//       Type.writeValueWithType(output, componentObject, context);
//     }
//   }
    throw new UnsupportedOperationException("To be implemented!");
  }

  @Override
  public void superDeserialize(Object result, DataInputStream input, IReaderContext context) {
    throw new UnsupportedOperationException("To be implemented!");
  }

  @Override
  public void superSerialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception {
    throw new UnsupportedOperationException("To be implemented!");
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
//    if(!obj.getClass().isArray()) throw new MemoriaException("array expected but was " + obj);
//    
//    int length = Array.getLength(obj);
//    for(int i = 0; i < length; ++i) {
//      Object object = Array.get(obj, i);
//      if (Type.typeClass != Type.getType(object)) continue; 
//        traversal.handle(object);
//    }
  }

//  private IObjectId componentTypeToId(ISerializeContext context, Class<?> componentType) {
//    ArrayComponentType internalType = ArrayComponentType.get(componentType);
//    if (internalType == null) return context.getMemoriaClassId(componentType);  
//    
//    IObjectId result = ((-1 * internalType.ordinal()));
//    return  result;
//  }
//  
//  private Class<?> getClassFromTypeInfo(IObjectId typeInfo, IReaderContext context) {
//    if (typeInfo < 0) {
//      int realType = (int) Math.abs(typeInfo);
//      return ArrayComponentType.values()[realType].getJavaClass();
//    }
//    return ((IMemoriaClass)context.getObjectById(typeInfo)).getJavaClass();
//  }

}
