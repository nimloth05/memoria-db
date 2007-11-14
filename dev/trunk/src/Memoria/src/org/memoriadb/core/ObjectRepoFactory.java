package org.memoriadb.core;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.handler.def.*;
import org.memoriadb.core.handler.list.ListHandler;
import org.memoriadb.core.id.*;
import org.memoriadb.core.meta.*;

public final class ObjectRepoFactory {

  public static ObjectRepo create(IObjectIdFactory idFactory) {
    ObjectRepo repo = new ObjectRepo(idFactory);
    registerMetaClasses(repo, idFactory);
    return repo;
  }

  /**
   * @param handler The handler to handle objects of type <tt>className</tt>.
   * @param className Name of the class the given <tt>handler</tt> can deal with.
   */
  private static void registerHandler(ObjectRepo repo, IDefaultObjectIdProvider factory, ISerializeHandler handler, String className) {
    IMemoriaClassConfig classConfig = new MemoriaHandlerClass(handler, className, factory.getHandlerMetaClass());
    repo.add(classConfig, classConfig.getMemoriaClassId());
  }
  
  private static void registerMetaClasses(ObjectRepo repo, IDefaultObjectIdProvider factory) {
    IMemoriaClassConfig fieldMetaClass = new MemoriaHandlerClass(new FieldClassHandler(), MemoriaFieldClass.class.getName(), factory.getHandlerMetaClass());
    IMemoriaClassConfig handlerMetaClass = new MemoriaHandlerClass(new HandlerClassHandler(), MemoriaHandlerClass.class.getName(), factory.getHandlerMetaClass());
    
    repo.add(factory.getMemoriaMetaClass(), fieldMetaClass.getMemoriaClassId(), fieldMetaClass);
    repo.add(factory.getHandlerMetaClass(), handlerMetaClass.getMemoriaClassId(), handlerMetaClass);
    
    // handlers for specific library-classes
    repo.add(factory.getArrayMemoriaClass(), factory.getHandlerMetaClass(), new MemoriaHandlerClass(new ArrayHandler(), Array.class.getName(), factory.getHandlerMetaClass()));

    //These classObjects don't need a fix known ID.
    IMemoriaClassConfig objectMemoriaClass = MemoriaFieldClassFactory.createMetaClass(Object.class, factory.getHandlerMetaClass());
    repo.add(objectMemoriaClass, objectMemoriaClass.getMemoriaClassId());
    
    registerHandler(repo, factory, new ListHandler(ArrayList.class.getName()), ArrayList.class.getName());
    registerHandler(repo, factory, new ListHandler(LinkedList.class.getName()), LinkedList.class.getName());
    registerHandler(repo, factory, new ListHandler(CopyOnWriteArrayList.class.getName()), CopyOnWriteArrayList.class.getName());
    registerHandler(repo, factory, new ListHandler(Stack.class.getName()), Stack.class.getName());
    registerHandler(repo, factory, new ListHandler(Vector.class.getName()), Vector.class.getName());
  }

  private ObjectRepoFactory() {}

}
