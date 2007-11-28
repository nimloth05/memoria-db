package org.memoriadb.handler.map;

import java.io.*;
import java.util.Map;

import org.memoriadb.core.IObjectTraversal;
import org.memoriadb.core.exception.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.meta.*;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.handler.IHandler;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

/**
 * Handles all subclasses of {@link java.util.List}.
 * 
 * @author msc
 * 
 */
public abstract class MapHandler implements IHandler {

  public interface IObjectResolver {
    public Object getObject(IReaderContext context);
  }

  private class PrimitiveResolver implements IObjectResolver {

    private final Object fObject;

    public PrimitiveResolver(Object object) {
      fObject = object;
    }

    @Override
    public Object getObject(IReaderContext context) {
      return fObject;
    }

  }

  private class ReferenceResolver implements IObjectResolver {

    private final IObjectId fId;

    public ReferenceResolver(IObjectId id) {
      fId = id;
    }

    @Override
    public Object getObject(IReaderContext context) {
      return context.getExistingObject(fId);
    }

  }

  @Override
  public void checkCanInstantiateObject(String className, IInstantiator instantiator) {
    if (!getClassName().equals(className)) throw new SchemaException("I am a handler for type " + getClassName() + " but I was called for "
        + className);
  }

  @Override
  public Object deserialize(DataInputStream input, final IReaderContext context, IObjectId typeId) throws Exception {
    Map<Object, Object> map = createMap();
    
    while (input.available() > 0) {

      IObjectResolver key = readNextElement(input, context);
      IObjectResolver value = readNextElement(input, context);

      context.objectToBind(new MapBindable(map, key, value));
    }

    return context.isInDataMode()? new MapDataObject(map, typeId) : map;
  }

  @Override
  public void serialize(Object obj, DataOutputStream output, ISerializeContext context) throws Exception {
    Map<?,?> map = getMapObject(obj);
    
    for (Object key: map.keySet()) {
      writeListEntry(key, output, context);
      writeListEntry(map.get(key), output, context);
    }
  }
  
  @Override
  public void traverseChildren(Object obj, IObjectTraversal traversal) {
    Map<?,?> map = getMapObject(obj);
    traverse(map.keySet(), traversal);
    traverse(map.values(), traversal);
  }
  
  protected Map<Object, Object> createMap() {
    try {
      return ReflectionUtil.createInstance(getClassName());
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }

  private Map<?,?> getMapObject(Object obj) {
    if (obj instanceof IMapDataObject) return ((IMapDataObject) obj).getMap();
    return (Map<?,?>) obj;
  }

  private IObjectResolver readNextElement(DataInputStream input, final IReaderContext context) {
    return Type.readValueWithType(input, context, new ITypeVisitor() {

      private IObjectResolver fResult;

      @Override
      public void visitClass(Type type, IObjectId objectId) {
        fResult = context.isNullReference(objectId) ? new PrimitiveResolver(null) : new ReferenceResolver(objectId);
      }

      @Override
      public void visitPrimitive(Type type, Object value) {
        fResult = new PrimitiveResolver(value);
      }
      
    }).fResult;
  }

  private void traverse(Iterable<?> iterable, IObjectTraversal traversal) {
    for (Object element : iterable) {
      if (element == null) continue;
      if (!Type.getType(element).isPrimitive()) traversal.handle(element);
    }
  }

  private void writeListEntry(Object listEntry, DataOutputStream output, ISerializeContext context) {
    if (listEntry == null) {
      Type.writeValueWithType(output, listEntry, context, Type.typeClass);
      return;
    }
    Type.writeValueWithType(output, listEntry, context);

  }

}
