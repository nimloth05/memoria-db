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
    IMetaClass arrayMetaClass = new HandlerMetaClass(new ArrayHandler(), Array.class); //Stub class. We need another implementation for array metaClass
    IMetaClass javaObjectMetaObject = new MetaClass(Object.class);
    
    IMetaClass arrayListMetaClass = new HandlerMetaClass(new ArrayListHandler(), ArrayList.class);
    
    repo.add(IMetaClass.METACLASS_OBJECT_ID, metaClassClassObject);
    repo.add(IMetaClass.HANDLER_META_CLASS_OBJECT_ID, handlerMetaClassObject);
    repo.add(IMetaClass.ARRAY_META_CLASS, arrayMetaClass);
    repo.add(IMetaClass.JAVA_OBJECT_META_OBJECT_ID, javaObjectMetaObject);
    repo.add(IMetaClass.JAVA_OBJECT_META_OBJECT_ID+1, arrayListMetaClass);
  }

  private ObjectRepoFactory() {}

}
