package org.memoriadb.core;

import org.memoriadb.core.meta.*;
import org.memoriadb.handler.array.ArrayHandler;
import org.memoriadb.handler.field.FieldbasedClassHandler;
import org.memoriadb.id.*;

public final class ObjectRepoFactory {

  public static ObjectRepository create(IObjectIdFactory idFactory) {
    ObjectRepository repo = new ObjectRepository(idFactory);
    registerMetaClasses(repo, idFactory);
    return repo;
  }

  private static void registerMetaClasses(ObjectRepository repo, IIdProvider factory) {
    // super meta mega class.
    IMemoriaClassConfig handlerMetaClass = new HandlerbasedMemoriaClass(new HandlerClassHandler(), factory.getHandlerMetaClass(), false);
    repo.add(factory.getHandlerMetaClass(), handlerMetaClass);

    IMemoriaClassConfig fieldMetaClass = new HandlerbasedMemoriaClass(new FieldbasedClassHandler(), factory.getHandlerMetaClass(), false);
    repo.add(factory.getFieldMetaClass(), fieldMetaClass);

    // array-handler
    repo.add(factory.getArrayMemoriaClass(), new HandlerbasedMemoriaClass(new ArrayHandler(), factory.getHandlerMetaClass(), false));
  }

  private ObjectRepoFactory() {}

}
