package org.memoriadb.core.handler.def;

import java.io.*;
import java.lang.reflect.Array;

import org.memoriadb.core.*;
import org.memoriadb.core.binder.BindArray;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.exception.MemoriaException;

public class ArrayHandler implements ISerializeHandler {

  @Override
  public Object desrialize(DataInputStream input, IReaderContext context) throws IOException {
    long compundTypeId = input.readLong();
    int arrayLength = input.readInt();
    IMetaClass metaClass = (IMetaClass) context.getObjectById(compundTypeId);
    Object array = java.lang.reflect.Array.newInstance(metaClass.getJavaClass(), arrayLength);
    
    long[] objectIds = new long[arrayLength]; 
    for(int index = 0; input.available() > 0; ++index) {
      objectIds[index] = input.readLong();
    }
    
    context.objectToBind(new BindArray(array, objectIds));
    
    return array;
  }

  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws IOException {
   if (!obj.getClass().isArray()) throw new MemoriaException("Object is not an array: " + obj);
   
   Class<?> componentType = obj.getClass().getComponentType();
   long componentTypeId = context.serializeIfNotContained(componentType);
   int arrayLength = Array.getLength(obj);
   
   output.writeLong(componentTypeId);
   output.writeInt(arrayLength);
   for(int i = 0; i < arrayLength; ++i) {
     Object componentObject = Array.get(obj, i);
     output.writeLong(context.serializeIfNotContained(componentObject));
   }
  }

}
