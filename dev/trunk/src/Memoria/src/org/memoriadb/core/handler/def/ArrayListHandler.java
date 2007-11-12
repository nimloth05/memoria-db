package org.memoriadb.core.handler.def;

import java.io.*;
import java.util.ArrayList;

import org.memoriadb.core.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.load.binder.ArrayListBindable;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.SchemaCorruptException;

public class ArrayListHandler implements ISerializeHandler {

  @Override
  public void checkCanInstantiateObject(String className, IDefaultInstantiator defaultInstantiator) {
    if (!ArrayList.class.getName().equals(className)) throw new SchemaCorruptException("I am a handler for type " + ArrayList.class.getName() +" but I was called for " + className);
  }

  @Override
  public Object deserialize(DataInputStream input, final IReaderContext context, IObjectId typeId) throws Exception {
    ArrayList<Object> list = new ArrayList<Object>();
    while (input.available() > 0) {
      
      Type.readValueWithType(input, context, new TypeVisitorHelper<Void, ArrayList<Object>>(list, context) {

        @Override
        public void visitClass(Type type, IObjectId objectId) {
          fContext.objectToBind(new ArrayListBindable(fMember, objectId));
        }

        @Override
        public void visitPrimitive(Type type, Object value) {
          fMember.add(value);
        }
      });
    }
    
    Object result = list;
    if (context.getMode() == DBMode.data) {
      result = new ListDataObject(list, typeId);
    }
    return result;
  }

  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception {
    ArrayList<?> list = getListObject(obj);
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

  private ArrayList<?> getListObject(Object obj) {
    if (obj instanceof IListDataObject) return (ArrayList<?>) ((IListDataObject)obj).getList();
    return (ArrayList<?>) obj;
  }

}
