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
    IMemoriaClass fieldMetaClass = new MemoriaHandlerClass(new FieldClassHandler(), MemoriaFieldClass.class);
    IMemoriaClass handlerMetaClass = new MemoriaHandlerClass(new HandlerClassHandler(), MemoriaHandlerClass.class);
    
    repo.add(factory.getMemoriaMetaClass(), factory.getMemoriaMetaClass(), fieldMetaClass);
    repo.add(factory.getHandlerMetaClass(), factory.getHandlerMetaClass(), handlerMetaClass);
    
    // handlers for specific library-classes
    
    repo.add(factory.getArrayMemoriaClass(), factory.getHandlerMetaClass(), new MemoriaHandlerClass(new ArrayHandler(), Array.class));

    //These classObjects don't need a fix known ID.
    repo.add(MemoriaFieldClassFactory.createMetaClass(Object.class));
    repo.add(new MemoriaHandlerClass(new ArrayListHandler(), ArrayList.class));
  }

  private ObjectRepoFactory() {}

}
