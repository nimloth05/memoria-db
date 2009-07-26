package org.memoriadb.core;

import org.memoriadb.core.meta.HandlerClassHandler;
import org.memoriadb.core.meta.IMemoriaClassConfig;
import org.memoriadb.core.meta.MemoriaClass;
import org.memoriadb.handler.array.ArrayHandler;
import org.memoriadb.handler.field.FieldBasedHandlerHandler;
import org.memoriadb.id.IIdProvider;
import org.memoriadb.id.IObjectIdFactory;

public final class ObjectRepoFactory {

  public static ObjectRepository create(IObjectIdFactory idFactory) {
    ObjectRepository repo = new ObjectRepository(idFactory);
    registerMetaClasses(repo, idFactory);
    return repo;
  }

  private static void registerMetaClasses(ObjectRepository repo, IIdProvider idProvider) {
    // super meta mega class.
    IMemoriaClassConfig handlerMetaClass = new MemoriaClass(new HandlerClassHandler(), idProvider.getHandlerMetaClass(), false);
    repo.add(idProvider.getHandlerMetaClass(), handlerMetaClass);

    IMemoriaClassConfig fieldMetaClass = new MemoriaClass(new FieldBasedHandlerHandler(), idProvider.getHandlerMetaClass(), false);
    repo.add(idProvider.getFieldMetaClass(), fieldMetaClass);

    // array-handler
    repo.add(idProvider.getArrayMemoriaClass(), new MemoriaClass(new ArrayHandler(), idProvider.getHandlerMetaClass(), false));
  }

  private ObjectRepoFactory() {}

}
