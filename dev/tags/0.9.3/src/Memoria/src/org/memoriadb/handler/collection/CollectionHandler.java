/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.handler.collection;

import java.io.DataOutput;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.SchemaException;
import org.memoriadb.core.file.IWriterContext;
import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.core.util.io.IDataInput;
import org.memoriadb.handler.*;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

/**
 * Handles all subclasses of {@link java.util.List}.
 * 
 * @author Sandro
 * 
 */
public abstract class CollectionHandler<T extends Collection<Object>> implements IHandler {

  public static class ConcurrentSkipListSetHandler extends SetHandler {

    public ConcurrentSkipListSetHandler() {
      super(ConcurrentSkipListSet.class);
    }

    @Override
    protected Set<Object> createCollectionForDataMode() {
      return new HashSet<Object>();
    }
  }

  public static class ListHandler extends CollectionHandler<List<Object>> {

    public <T extends List<?>> ListHandler(Class<T> clazz) {
      super(clazz);
    }

    public ListHandler(String name) {
      super(name);
    }

    @Override
    protected IDataObject createDataObject(List<Object> collection, IObjectId typeId) {
      return new ListDataObject(collection, typeId);
    }
  }

  public static class SetHandler extends CollectionHandler<Set<Object>> {

    public <T extends Set<?>> SetHandler(Class<T> clazz) {
      super(clazz);
    }

    public SetHandler(String name) {
      super(name);
    }

    @Override
    protected IDataObject createDataObject(Set<Object> collection, IObjectId typeId) {
      return new SetDataObject(collection, typeId);
    }
  }

  public static class TreeSetHandler extends SetHandler {

    public TreeSetHandler() {
      super(TreeSet.class);
    }

    @Override
    protected Set<Object> createCollectionForDataMode() {
      return new HashSet<Object>();
    }

  }

  private final Class<?> fClass;
  private final String fClassName;

  public <X extends Collection<?>> CollectionHandler(Class<X> clazz) {
    this(clazz, clazz.getName());
  }

  public CollectionHandler(String className) {
    this(ReflectionUtil.getClass(className), className);
  }
  
  private CollectionHandler(Class<?> clazz, String className) {
    fClass = clazz;
    fClassName = className;
  }

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {
    if (!getClassName().equals(className)) 
        throw new SchemaException("I am a handler for type " + getClassName() + " but I was called for " + className);
  }

  @Override
  public Object deserialize(IDataInput input, final IReaderContext context, IObjectId typeId) throws Exception {
    final T collection = createCollection(context.isInDataMode());
    int size = input.readInt();
    
    for(int i = 0; i < size; ++i) {

      Type.readValueWithType(input, context, new ITypeVisitor() {

        @Override
        public void visitClass(Type type, IObjectId objectId) {
          context.addGenTwoBinding(new ObjectCollectionBindable(collection, objectId));
        }

        @Override
        public void visitNull() {
          context.addGenTwoBinding(new NullCollectionBindlable(collection));
        }

        @Override
        public void visitPrimitive(Type type, Object value) {
          context.addGenTwoBinding(new CollectionBindable(collection, value));
        }

        @Override
        public void visitValueObject(Object value) {
          context.addGenTwoBinding(new CollectionBindable(collection, value));
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
  public void serialize(Object obj, DataOutput output, IWriterContext context) throws Exception {
    Collection<?> list = getCollectionObject(obj);
    
    output.writeInt(list.size());
    
    for (Object listEntry : list) {
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

  protected final T createCollection(boolean isDataMode) {
    return isDataMode ? createCollectionForDataMode() : createCollectionForObjectMode();
  }

  protected T createCollectionForDataMode() {
    return createCollectionForObjectMode();
  }

  protected T createCollectionForObjectMode() {
    return ReflectionUtil.<T>createInstance(fClass);
  }

  protected abstract IDataObject createDataObject(T collection, IObjectId typeId);

  private Collection<?> getCollectionObject(Object obj) {
    if (obj instanceof ICollectionDataObject) return ((ICollectionDataObject) obj).getCollection();
    return (Collection<?>) obj;
  }

}
