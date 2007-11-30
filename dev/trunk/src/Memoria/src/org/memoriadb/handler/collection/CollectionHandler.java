package org.memoriadb.handler.collection;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.SchemaException;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.handler.*;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

/**
 * Handles all subclasses of {@link java.util.List}.
 * 
 * @author msc
 * 
 */
public abstract class CollectionHandler implements IHandler {

  public static class ConcurrentSkipListSetHandler extends SetHandler {

    public ConcurrentSkipListSetHandler() {
      super(ConcurrentSkipListSet.class);
    }

    @Override
    protected Collection<Object> createCollectionForDataMode() {
      return new HashSet<Object>();
    }
  }

  public static class ListHandler extends CollectionHandler {

    public <T extends List<?>> ListHandler(Class<T> clazz) {
      super(clazz);
    }

    public ListHandler(String name) {
      super(name);
    }

    @Override
    protected IDataObject createDataObject(Collection<Object> collection, IObjectId typeId) {
      return new ListDataObject((List<Object>) collection, typeId);
    }
  }

  public static class SetHandler extends CollectionHandler {

    public <T extends Set<?>> SetHandler(Class<T> clazz) {
      super(clazz);
    }

    public SetHandler(String name) {
      super(name);
    }

    @Override
    protected IDataObject createDataObject(Collection<Object> collection, IObjectId typeId) {
      return new SetDataObject((Set<Object>) collection, typeId);
    }
  }

  public static class TreeSetHandler extends SetHandler {

    public TreeSetHandler() {
      super(TreeSet.class);
    }

    @Override
    protected Collection<Object> createCollectionForDataMode() {
      return new HashSet<Object>();
    }

  }

  private final String fClassName;

  public <T extends Collection<?>> CollectionHandler(Class<T> clazz) {
    this(clazz.getName());
  }

  public CollectionHandler(String className) {
    fClassName = className;
  }

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {
    if (!getClassName().equals(className)) throw new SchemaException("I am a handler for type " + getClassName() + " but I was called for "
        + className);
  }

  @Override
  public Object deserialize(DataInputStream input, final IReaderContext context, IObjectId typeId) throws Exception {
    final Collection<Object> collection = createCollection(context.isInDataMode());
    while (input.available() > 0) {

      Type.readValueWithType(input, context, new ITypeVisitor() {

        @Override
        public void visitClass(Type type, IObjectId objectId) {
          context.objectToBind(new CollectionBindable(collection, objectId));
        }

        @Override
        public void visitNull() {
          collection.add(null);
        }

        @Override
        public void visitPrimitive(Type type, Object value) {
          collection.add(value);
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
  public String getClassName() {
    return fClassName;
  }

  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception {
    Collection<?> list = getCollectionObject(obj);
    for (Object listEntry : list) {
      if (listEntry == null) {
        Type.writeValueWithType(output, listEntry, context, Type.typeClass);
        continue;
      }
      Type.writeValueWithType(output, listEntry, context);
    }
  }

  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    Collection<?> list = getCollectionObject(obj);

    for (Object listEntry : list) {
      if (listEntry == null) continue;
      if (!Type.getType(listEntry).isPrimitive()) traversal.handle(listEntry);
    }
  }

  protected final Collection<Object> createCollection(boolean isDataMode) {
    return isDataMode ? createCollectionForDataMode() : createCollectionForObjectMode();
  }

  protected Collection<Object> createCollectionForDataMode() {
    return createCollectionForObjectMode();
  }

  protected Collection<Object> createCollectionForObjectMode() {
    return ReflectionUtil.createInstance(getClassName());
  }

  protected abstract IDataObject createDataObject(Collection<Object> collection, IObjectId typeId);

  private Collection<?> getCollectionObject(Object obj) {
    if (obj instanceof ICollectionDataObject) return ((ICollectionDataObject) obj).getCollection();
    return (Collection<?>) obj;
  }

}
