package org.memoriadb.core;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.handler.def.*;
import org.memoriadb.core.handler.list.ListHandler;
import org.memoriadb.core.id.*;
import org.memoriadb.core.meta.*;
import org.memoriadb.util.ReflectionUtil;

public final class ObjectRepoFactory {

  public static ObjectRepo create(IObjectIdFactory idFactory, List<String> customHandlers) {
    ObjectRepo repo = new ObjectRepo(idFactory);
    registerMetaClasses(repo, idFactory, customHandlers);
    return repo;
  }

  private static void addCustomHandlers(ObjectRepo repo, IDefaultObjectIdProvider factory, List<String> customHandlers) {
    for (String className : customHandlers) {
      registerHandler(repo, factory, (ISerializeHandler)ReflectionUtil.createInstance(className));
    }
  }

  /**
   * @param handler
   *          The handler to handle objects of type <tt>className</tt>.
   * @param className
   *          Name of the class the given <tt>handler</tt> can deal with.
   */
  private static void registerHandler(ObjectRepo repo, IDefaultObjectIdProvider factory, ISerializeHandler handler) {
    IMemoriaClassConfig classConfig = new MemoriaHandlerClass(handler, factory.getHandlerMetaClass());
    repo.add(classConfig, classConfig.getMemoriaClassId());
  }

  /**
   * @param handler
   *          The handler to handle objects of type <tt>className</tt>.
   * @param className
   *          Name of the class the given <tt>handler</tt> can deal with.
   */
  private static void registerListHandler(ObjectRepo repo, IDefaultObjectIdProvider factory, Class<?> listClass) {
    registerHandler(repo, factory, new ListHandler(listClass.getName()));
  }

  private static void registerMetaClasses(ObjectRepo repo, IDefaultObjectIdProvider factory, List<String> customHandlers) {
    IMemoriaClassConfig fieldMetaClass = new MemoriaHandlerClass(new FieldClassHandler(), factory.getHandlerMetaClass());
    IMemoriaClassConfig handlerMetaClass = new MemoriaHandlerClass(new HandlerClassHandler(), factory.getHandlerMetaClass());

    repo.add(factory.getMemoriaMetaClass(), fieldMetaClass.getMemoriaClassId(), fieldMetaClass);
    repo.add(factory.getHandlerMetaClass(), handlerMetaClass.getMemoriaClassId(), handlerMetaClass);

    // handlers for specific library-classes
    repo.add(factory.getArrayMemoriaClass(), factory.getHandlerMetaClass(), new MemoriaHandlerClass(new ArrayHandler(), factory
        .getHandlerMetaClass()));

    // These classObjects don't need a fix known ID.
    IMemoriaClassConfig objectMemoriaClass = MemoriaFieldClassFactory.createMetaClass(Object.class, factory.getHandlerMetaClass());
    repo.add(objectMemoriaClass, objectMemoriaClass.getMemoriaClassId());

    registerListHandler(repo, factory, ArrayList.class);
    registerListHandler(repo, factory, LinkedList.class);
    registerListHandler(repo, factory, CopyOnWriteArrayList.class);
    registerListHandler(repo, factory, Stack.class);
    registerListHandler(repo, factory, Vector.class);

    addCustomHandlers(repo, factory, customHandlers);

  }

  private ObjectRepoFactory() {}

}
