package org.memoriadb.core.repo;

import java.lang.reflect.Array;

import org.memoriadb.core.handler.def.*;
import org.memoriadb.core.meta.*;

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
    
    repo.addBootstrapped(IMetaClass.METACLASS_OBJECT_ID, metaClassClassObject);
    repo.addBootstrapped(IMetaClass.HANDLER_META_CLASS_OBJECT_ID, handlerMetaClassObject);
    repo.addBootstrapped(IMetaClass.ARRAY_META_CLASS, arrayMetaClass);
  }

  private ObjectRepoFactory() {}

}
