package org.memoriadb.core;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.memoriadb.core.handler.def.*;
import org.memoriadb.core.id.IObjectIdFactory;
import org.memoriadb.core.meta.*;
import org.memoriadb.util.IdConstants;

public final class ObjectRepoFactory {

  public static ObjectRepo create(IObjectIdFactory factory) {
    ObjectRepo repo = new ObjectRepo();
    registerMetaClasses(repo, factory);
    return repo;
  }

  private static void registerMetaClasses(ObjectRepo repo, IObjectIdFactory factory) {
    IMemoriaClass metaClassClassObject = new MemoriaHandlerClass(new MetaFieldClassHandler(), MemoriaFieldClass.class);
    IMemoriaClass handlerMetaClassObject = new MemoriaHandlerClass(new MetaClassHandler(), MemoriaHandlerClass.class);
    
    repo.add(factory.getMemoriaMetaClass(), metaClassClassObject);
    repo.add(IdConstants.HANDLER_META_CLASS_OBJECT_ID, handlerMetaClassObject);
    
    // handlers for specific library-classes
    
    repo.add(IdConstants.ARRAY_META_CLASS, new MemoriaHandlerClass(new ArrayHandler(), Array.class));

    //These classObjects don't need a fix known ID.
    repo.add(new MemoriaFieldClass(Object.class));
    repo.add(new MemoriaHandlerClass(new ArrayListHandler(), ArrayList.class));
  }

  private ObjectRepoFactory() {}

}
