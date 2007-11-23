package org.memoriadb.core.handler.collection;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import org.memoriadb.core.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.handler.*;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.meta.*;
import org.memoriadb.exception.*;
import org.memoriadb.util.ReflectionUtil;

/**
 * Handles all subclasses of {@link java.util.List}.
 * 
 * @author msc
 * 
 */
public abstract class CollectionHandler implements ISerializeHandler {

  public static class ArrayListHandler extends CollectionHandler {
    @Override
    public String getClassName() {
      return ArrayList.class.getName();
    }

    @Override
    protected IDataObject createDataObject(Collection<Object> collection, IObjectId typeId) {
      return new ListDataObject((List<Object>) collection, typeId);
    }
  }

  public static class ConcurrentSkipListSetHandler extends CollectionHandler {
    @Override
    public String getClassName() {
      return ConcurrentSkipListSet.class.getName();
    }

    @Override
    protected IDataObject createDataObject(Collection<Object> collection, IObjectId typeId) {
      return new ListDataObject((List<Object>) collection, typeId);
    }
  }

  public static class CopyOnWriteListHandler extends CollectionHandler {
    @Override
    public String getClassName() {
      return CopyOnWriteArrayList.class.getName();
    }

    @Override
    protected IDataObject createDataObject(Collection<Object> collection, IObjectId typeId) {
      return new ListDataObject((List<Object>) collection, typeId);
    }
  }

  public static class HashSetHandler extends CollectionHandler {

    @Override
    public String getClassName() {
      return HashSet.class.getName();
    }

    @Override
    protected IDataObject createDataObject(Collection<Object> collection, IObjectId typeId) {
      return new SetDataObject((Set<Object>) collection, typeId);
    }

  }

  public static class LinkedHashSetHandler extends CollectionHandler {

    @Override
    public String getClassName() {
      return LinkedHashSet.class.getName();
    }

    @Override
    protected IDataObject createDataObject(Collection<Object> collection, IObjectId typeId) {
      return new SetDataObject((Set<Object>) collection, typeId);
    }
  }

  public static class LinkedListHandler extends CollectionHandler {
    @Override
    public String getClassName() {
      return LinkedList.class.getName();
    }

    @Override
    protected IDataObject createDataObject(Collection<Object> collection, IObjectId typeId) {
      return new ListDataObject((List<Object>) collection, typeId);
    }
  }

  public static class StackHandler extends CollectionHandler {
    @Override
    public String getClassName() {
      return Stack.class.getName();
    }

    @Override
    protected IDataObject createDataObject(Collection<Object> collection, IObjectId typeId) {
      return new ListDataObject((List<Object>) collection, typeId);
    }
  }

  public static class TreeSetHandler extends CollectionHandler {

    @Override
    public String getClassName() {
      return TreeSet.class.getName();
    }

    @Override
    protected IDataObject createDataObject(Collection<Object> collection, IObjectId typeId) {
      return new SetDataObject((Set<Object>) collection, typeId);
    }
  }

  public static class VectorHandler extends CollectionHandler {
    @Override
    public String getClassName() {
      return Vector.class.getName();
    }

    @Override
    protected IDataObject createDataObject(Collection<Object> collection, IObjectId typeId) {
      return new ListDataObject((List<Object>) collection, typeId);
    }
  }

  @Override
  public void checkCanInstantiateObject(String className, IDefaultInstantiator defaultInstantiator) {
    if (!getClassName().equals(className)) throw new SchemaException("I am a handler for type " + getClassName() + " but I was called for "
        + className);
  }

  @Override
  public Object deserialize(DataInputStream input, final IReaderContext context, IObjectId typeId) throws Exception {
    Collection<Object> collection = createCollection();
    while (input.available() > 0) {

      Type.readValueWithType(input, context, new TypeVisitorHelper<Void, Collection<Object>>(collection, context) {

        @Override
        public void visitClass(Type type, IObjectId objectId) {
          fContext.objectToBind(new CollectionBindable(fMember, objectId));
        }

        @Override
        public void visitPrimitive(Type type, Object value) {
          fMember.add(value);
        }
      });
    }

    Object result = collection;
    if (context.isInDataMode()) {
      result = createDataObject(collection, typeId);
    }
    return result;
  }

  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception {
    Collection<?> list = getListObject(obj);
    for (Object listEntry : list) {
      Type.writeValueWithType(output, listEntry, context);
    }
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    Collection<?> list = (Collection<?>) obj;

    for (Object listEntry : list) {
      if (Type.getType(listEntry) == Type.typeClass) traversal.handle(listEntry);
    }
  }

  protected Collection<Object> createCollection() {
    try {
      return ReflectionUtil.createInstance(getClassName());
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }

  protected abstract IDataObject createDataObject(Collection<Object> collection, IObjectId typeId);

  private Collection<?> getListObject(Object obj) {
    if (obj instanceof ICollectionDataObject) return ((ICollectionDataObject) obj).getCollection();
    return (Collection<?>) obj;
  }

}
