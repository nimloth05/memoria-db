package org.memoriadb.core.handler.def;

import java.io.*;
import java.util.ArrayList;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.load.binder.ArrayListBindable;
import org.memoriadb.core.meta.Type;

public class ArrayListHandler implements ISerializeHandler {

  @Override
  public Object deserialize(DataInputStream input, IReaderContext context) throws Exception {
    ArrayList<Object> result = new ArrayList<Object>();
    while (input.available() > 0) {
      Type type = Type.readType(input, context);
      
      Object readValue = type.readValue(input);
      if (type == Type.typeClass) {
        context.objectToBind(new ArrayListBindable(result, (Long) readValue));
        continue;
      }
      
      result.add(readValue);
    }
    return result;
  }

  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception {
    ArrayList<?> list = (ArrayList<?>) obj;
    for(Object listEntry: list) {
      Type.writeValueWithType(output, listEntry, context);
    }
  }

  @Override
  public void superDeserialize(Object result, DataInputStream input, IReaderContext context) {
    throw new UnsupportedOperationException("To be implemented! Write Test-first");
  }

  @Override
  public void superSerialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception {
    throw new UnsupportedOperationException("To be implemented! Write Test-first");    
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    ArrayList<?> list = (ArrayList<?>) obj;
    
    for(Object listEntry: list) {
      if (Type.getType(listEntry) == Type.typeClass) traversal.handle(listEntry);
    }
  }

}
