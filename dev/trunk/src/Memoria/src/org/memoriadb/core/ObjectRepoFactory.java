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
    IMetaClass metaClassClassObject = new HandlerMetaClass(new MetaFieldClassHandler(), MetaClass.class);
    IMetaClass handlerMetaClassObject = new HandlerMetaClass(new MetaClassHandler(), HandlerMetaClass.class);
    
    repo.add(IdConstants.METACLASS_OBJECT_ID, metaClassClassObject);
    repo.add(IdConstants.HANDLER_META_CLASS_OBJECT_ID, handlerMetaClassObject);
    
    // handlers for specific library-classes
    
    repo.add(IdConstants.ARRAY_META_CLASS, new HandlerMetaClass(new ArrayHandler(), Array.class));

    //These classObjects don't need a fix known ID.
    repo.add(new MetaClass(Object.class));
    repo.add(new HandlerMetaClass(new ArrayListHandler(), ArrayList.class));
  }

  private ObjectRepoFactory() {}

}
