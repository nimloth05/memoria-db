package org.memoriadb.core;

import java.lang.reflect.Array;

import org.memoriadb.core.handler.def.*;

public final class ObjectRepoFactory {
  
  public static ObjectRepo create() {
    ObjectRepo repo = new ObjectRepo();
    boostrap(repo);
    return repo;
  }
  
  
  private static void boostrap(ObjectRepo repo) {
    IMetaClass metaClassClassObject = new HandlerMetaClass(new MetaFieldClassHandler(), MetaClass.class);
    IMetaClass handlerMetaClassObject = new HandlerMetaClass(new MetaClassHandler(), HandlerMetaClass.class);
    IMetaClass arrayMetaClass = new HandlerMetaClass(new ArrayHandler(), Array.class); //Stub class. We need another implementation for array metaClass
    
    repo.put(IMetaClass.METACLASS_OBJECT_ID, metaClassClassObject);
    repo.put(IMetaClass.HANDLER_META_CLASS_OBJECT_ID, handlerMetaClassObject);
    repo.put(IMetaClass.ARRAY_META_CLASS, arrayMetaClass);
  }

  private ObjectRepoFactory() {}

}
