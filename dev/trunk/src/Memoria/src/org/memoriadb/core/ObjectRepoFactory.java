package org.memoriadb.core;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.memoriadb.core.handler.def.*;
import org.memoriadb.core.id.*;
import org.memoriadb.core.meta.*;

public final class ObjectRepoFactory {

  public static ObjectRepo create(IObjectIdFactory idFactory) {
    ObjectRepo repo = new ObjectRepo(idFactory);
    registerMetaClasses(repo, idFactory);
    return repo;
  }

  private static void registerMetaClasses(ObjectRepo repo, IDefaultObjectIdProvider factory) {
    IMemoriaClassConfig fieldMetaClass = new MemoriaHandlerClass(new FieldClassHandler(), MemoriaFieldClass.class, factory.getHandlerMetaClass());
    IMemoriaClassConfig handlerMetaClass = new MemoriaHandlerClass(new HandlerClassHandler(), MemoriaHandlerClass.class, factory.getHandlerMetaClass());
    
    repo.add(factory.getMemoriaMetaClass(), fieldMetaClass.getMemoriaClassId(), fieldMetaClass);
    repo.add(factory.getHandlerMetaClass(), handlerMetaClass.getMemoriaClassId(), handlerMetaClass);
    
    // handlers for specific library-classes
    repo.add(factory.getArrayMemoriaClass(), factory.getHandlerMetaClass(), new MemoriaHandlerClass(new ArrayHandler(), Array.class, factory.getHandlerMetaClass()));

    //These classObjects don't need a fix known ID.
    IMemoriaClassConfig objectMemoriaClass = MemoriaFieldClassFactory.createMetaClass(Object.class, factory.getHandlerMetaClass());
    repo.add(objectMemoriaClass, objectMemoriaClass.getMemoriaClassId());
    
    IMemoriaClassConfig arrayListMemoriaClass = new MemoriaHandlerClass(new ArrayListHandler(), ArrayList.class, factory.getHandlerMetaClass());
    repo.add(arrayListMemoriaClass, arrayListMemoriaClass.getMemoriaClassId());
  }

  private ObjectRepoFactory() {}

}
