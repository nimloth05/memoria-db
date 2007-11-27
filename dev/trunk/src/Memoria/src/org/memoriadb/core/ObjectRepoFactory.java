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
    // super meta mega class.
    IMemoriaClassConfig handlerMetaClass = new HandlerbasedMemoriaClass(new HandlerClassHandler(), factory.getHandlerMetaClass());
    repo.add(factory.getHandlerMetaClass(), handlerMetaClass);

    IMemoriaClassConfig fieldMetaClass = new HandlerbasedMemoriaClass(new FieldClassHandler(), factory.getHandlerMetaClass());
    repo.add(factory.getFieldMetaClass(), fieldMetaClass);

    // array-handler
    repo.add(factory.getArrayMemoriaClass(), new HandlerbasedMemoriaClass(new ArrayHandler(), factory.getHandlerMetaClass()));
  }

  private ObjectRepoFactory() {}

}
