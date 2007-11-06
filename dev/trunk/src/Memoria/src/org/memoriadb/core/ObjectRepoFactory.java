package org.memoriadb.core;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.memoriadb.core.handler.def.*;
import org.memoriadb.core.id.IDefaultObjectIdProvider;
import org.memoriadb.core.meta.*;

public final class ObjectRepoFactory {

  public static ObjectRepo create(IDefaultObjectIdProvider factory) {
    ObjectRepo repo = new ObjectRepo();
    registerMetaClasses(repo, factory);
    return repo;
  }

  private static void registerMetaClasses(ObjectRepo repo, IDefaultObjectIdProvider factory) {
    IMemoriaClass metaClassClassObject = new MemoriaHandlerClass(new MetaFieldClassHandler(), MemoriaFieldClass.class);
    IMemoriaClass handlerMetaClassObject = new MemoriaHandlerClass(new MetaClassHandler(), MemoriaHandlerClass.class);
    
    repo.add(factory.getMemoriaMetaClass(), metaClassClassObject);
    repo.add(factory.getHandlerMetaClass(), handlerMetaClassObject);
    
    // handlers for specific library-classes
    
    repo.add(factory.getArrayMemoriaClass(), new MemoriaHandlerClass(new ArrayHandler(), Array.class));

    //These classObjects don't need a fix known ID.
    repo.add(new MemoriaFieldClass(Object.class));
    repo.add(new MemoriaHandlerClass(new ArrayListHandler(), ArrayList.class));
  }

  private ObjectRepoFactory() {}

}
