/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
