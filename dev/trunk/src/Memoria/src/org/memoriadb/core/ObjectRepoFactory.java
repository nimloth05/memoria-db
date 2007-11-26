package org.memoriadb.core;

import org.memoriadb.core.handler.array.ArrayHandler;
import org.memoriadb.core.handler.def.HandlerClassHandler;
import org.memoriadb.core.handler.field.FieldClassHandler;
import org.memoriadb.core.id.*;
import org.memoriadb.core.meta.*;

public final class ObjectRepoFactory {

  public static ObjectRepository create(IObjectIdFactory idFactory) {
    ObjectRepository repo = new ObjectRepository(idFactory);
    registerMetaClasses(repo, idFactory);
    return repo;
  }

  private static void registerMetaClasses(ObjectRepository repo, IDefaultIdProvider factory) {
    IMemoriaClassConfig handlerMetaClass = new MemoriaHandlerClass(new HandlerClassHandler(), factory.getHandlerMetaClass());
    IMemoriaClassConfig fieldMetaClass = new MemoriaHandlerClass(new FieldClassHandler(), factory.getHandlerMetaClass());

    repo.add(factory.getFieldMetaClass(), fieldMetaClass.getMemoriaClassId(), fieldMetaClass);
    repo.add(factory.getHandlerMetaClass(), handlerMetaClass.getMemoriaClassId(), handlerMetaClass);

    // handlers for specific library-classes
    repo.add(factory.getArrayMemoriaClass(), factory.getHandlerMetaClass(), new MemoriaHandlerClass(new ArrayHandler(), factory
        .getHandlerMetaClass()));

  }

  private ObjectRepoFactory() {}

}
