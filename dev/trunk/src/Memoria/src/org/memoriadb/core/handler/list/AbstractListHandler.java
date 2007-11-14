package org.memoriadb.core.handler.list;

import java.io.*;
import java.util.List;

import org.memoriadb.core.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.handler.def.*;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.meta.*;

/**
 * Handles all subclasses of {@link java.util.List}.
 * @author msc
 *
 */
public abstract class AbstractListHandler implements ISerializeHandler {

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

  protected abstract List<Object> createList();

  private List<?> getListObject(Object obj) {
    if (obj instanceof IListDataObject) return ((IListDataObject)obj).getList();
    return (List<?>) obj;
  }

}
