package org.memoriadb.core;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.memoriadb.core.handler.def.*;
import org.memoriadb.core.meta.*;

public final class ObjectRepoFactory {

  public static ObjectRepo create() {
    ObjectRepo repo = new ObjectRepo();
    registerMetaClasses(repo);
    return repo;
  }

  private static void registerMetaClasses(ObjectRepo repo) {
    IMetaClass metaClassClassObject = new MemoriaHandlerClass(new MetaFieldClassHandler(), MemoriaFieldClass.class);
    IMetaClass handlerMetaClassObject = new MemoriaHandlerClass(new MetaClassHandler(), MemoriaHandlerClass.class);
    
    repo.add(IdConstants.METACLASS_OBJECT_ID, metaClassClassObject);
    repo.add(IdConstants.HANDLER_META_CLASS_OBJECT_ID, handlerMetaClassObject);
    
    // handlers for specific library-classes
    
    repo.add(IdConstants.ARRAY_META_CLASS, new MemoriaHandlerClass(new ArrayHandler(), Array.class));

    //These classObjects don't need a fix known ID.
    repo.add(new MemoriaFieldClass(Object.class));
    repo.add(new MemoriaHandlerClass(new ArrayListHandler(), ArrayList.class));
  }

  private ObjectRepoFactory() {}

}
