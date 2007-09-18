package org.memoriadb.core.handler.def;

import java.io.*;
import java.util.ArrayList;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.load.binder.ArrayListBindable;
import org.memoriadb.core.meta.*;

public class ArrayListHandler implements ISerializeHandler {

  @Override
  public Object deserialize(DataInputStream input, final IReaderContext context) throws Exception {
    ArrayList<Object> list = new ArrayList<Object>();
    while (input.available() > 0) {
      
      Type.readValueWithType(input, context, new TypeVisitorHelper<Void, ArrayList<Object>>(list, context) {

        @Override
        public void visitClass(Type type, long objectId) {
          fContext.objectToBind(new ArrayListBindable(fMember, objectId));
        }

        @Override
        public void visitPrimitive(Type type, Object value) {
          fMember.add(value);
        }
        
      });
    }
    return list;
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
