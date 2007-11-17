package org.memoriadb.core.handler.list;

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.memoriadb.core.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.handler.def.*;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.*;

/**
 * Handles all subclasses of {@link java.util.List}.
 * @author msc
 *
 */
public abstract class ListHandler implements ISerializeHandler {
  
  public static class ArrayListHandler extends ListHandler {
    @Override
    public String getClassName() {
      return ArrayList.class.getName();
    }
  }
  
  public static class CopyOnWriteListHandler extends ListHandler {
    @Override
    public String getClassName() {
      return CopyOnWriteArrayList.class.getName();
    }
  }
  
  public static class LinkedListHandler extends ListHandler {
    @Override
    public String getClassName() {
      return LinkedList.class.getName();
    }
  }
  
  public static class StackHandler extends ListHandler {
    @Override
    public String getClassName() {
      return Stack.class.getName();
    }
  }
  
  public static class VectorHandler extends ListHandler {
    @Override
    public String getClassName() {
      return Vector.class.getName();
    }
  }
  
  
  
  @Override
  public void checkCanInstantiateObject(String className, IDefaultInstantiator defaultInstantiator) {
    if (!getClassName().equals(className)) throw new SchemaCorruptException("I am a handler for type " + getClassName() +" but I was called for " + className);
  }

  @Override
  public Object deserialize(DataInputStream input, final IReaderContext context, IObjectId typeId) throws Exception {
    List<Object> list = createList();
    while (input.available() > 0) {
      
      Type.readValueWithType(input, context, new TypeVisitorHelper<Void, List<Object>>(list, context) {

        @Override
        public void visitClass(Type type, IObjectId objectId) {
          fContext.objectToBind(new ListBindable(fMember, objectId));
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
    List<?> list = getListObject(obj);
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
    List<?> list = (List<?>) obj;
    
    for(Object listEntry: list) {
      if (Type.getType(listEntry) == Type.typeClass) traversal.handle(listEntry);
    }
  }

  @SuppressWarnings("unchecked")
  protected List<Object> createList() {
    try {
      return (List<Object>) Class.forName(getClassName()).newInstance();
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }

  private List<?> getListObject(Object obj) {
    if (obj instanceof IListDataObject) return ((IListDataObject)obj).getList();
    return (List<?>) obj;
  }

}
