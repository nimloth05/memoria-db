/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.core;

import org.memoriadb.ITypeInfo;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.id.IObjectId;

/**
 * @author Sandro
 */
public class TypeInfo implements ITypeInfo {
 
  private final TransactionHandler fTransactionHandler;

  public TypeInfo(TransactionHandler transactionHandler) {
    fTransactionHandler = transactionHandler;
  }

  @Override
  public IObjectId addMemoriaClassIfNecessary(Class<?> clazz) {
    return fTransactionHandler.addMemoriaClassIfNecessary(clazz);
  }
  
  @Override
  public Iterable<IMemoriaClass> getAllClasses() {
    return fTransactionHandler.getAllClasses();
  }

  @Override
  public IObjectId getMemoriaArrayClass() {
    return fTransactionHandler.getMemoriaArrayClass();
  }

  @Override
  public IMemoriaClass getMemoriaClass(Class<?> clazz) {
    return fTransactionHandler.getMemoriaClass(clazz.getName());
  }

  @Override
  public IMemoriaClass getMemoriaClass(Object object) {
    return fTransactionHandler.getMemoriaClass(object);
  }

  @Override
  public IMemoriaClass getMemoriaClass(String className) {
    return fTransactionHandler.getMemoriaClass(className);
  }

  @Override
  public IObjectId getMemoriaClassId(Class<?> clazz) {
    return getMemoriaClassId(clazz.getName());
  }

  @Override
  public IObjectId getMemoriaClassId(Object object) {
    return fTransactionHandler.getMemoriaClassId(object);
  }

  @Override
  public IObjectId getMemoriaClassId(String className) {
    return fTransactionHandler.getMemoriaClassId(className);
  }

}
