package org.memoriadb.core;

import java.util.List;

import org.memoriadb.core.handler.def.*;
import org.memoriadb.core.id.*;
import org.memoriadb.core.meta.*;

public final class ObjectRepoFactory {

  public static ObjectRepo create(IObjectIdFactory idFactory, List<String> customHandlers) {
    ObjectRepo repo = new ObjectRepo(idFactory);
    registerMetaClasses(repo, idFactory, customHandlers);
    return repo;
  }

  private static void registerMetaClasses(ObjectRepo repo, IDefaultObjectIdProvider factory, List<String> customHandlers) {
    IMemoriaClassConfig fieldMetaClass = new MemoriaHandlerClass(new FieldClassHandler(), factory.getHandlerMetaClass());
    IMemoriaClassConfig handlerMetaClass = new MemoriaHandlerClass(new HandlerClassHandler(), factory.getHandlerMetaClass());

    repo.add(factory.getMemoriaMetaClass(), fieldMetaClass.getMemoriaClassId(), fieldMetaClass);
    repo.add(factory.getHandlerMetaClass(), handlerMetaClass.getMemoriaClassId(), handlerMetaClass);

    // handlers for specific library-classes
    repo.add(factory.getArrayMemoriaClass(), factory.getHandlerMetaClass(), new MemoriaHandlerClass(new ArrayHandler(), factory
        .getHandlerMetaClass()));

  }

  private ObjectRepoFactory() {}

}
