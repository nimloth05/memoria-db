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

package org.memoriadb.core.mode;

import org.memoriadb.core.IObjectRepository;
import org.memoriadb.core.TransactionHandler;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

/**
 * @author msc
 * 
 */
public interface IModeStrategy {
  /**
   * @param transactionHandler
   * @param transactionHandler
   * @param obj
   * @param obj
   * @return ObjectId of the MemoriaClass for the given obj
   */
  public IObjectId addMemoriaClassIfNecessary(TransactionHandler transactionHandler, Object obj);

  /**
   * Before an object is added to the ObjectRepository, it is checked for instantiability.
   * @param transactionHandler
   * @param transactionHandler
   * @param memoriaClassId
   * @param memoriaClassId
   * @param instantiator TODO
   */
  public void checkCanInstantiateObject(TransactionHandler transactionHandler, IObjectId memoriaClassId, IInstantiator instantiator);

  public void checkObject(Object obj);

  public Object createEnum(Enum<? extends Enum<?>> current, IObjectId memoriaClassId);

  public IMemoriaClass getMemoriaClass(Object object, IObjectRepository objectRepository);

  /**
   * @return true, if the db is operated in data-mode (the java classes are not required).
   */
  public boolean isDataMode();

  /**
   * @param obj
   * @return true, if the given <tt>obj</tt> is an enum.
   * @param obj
   */
  public boolean isEnum(Object obj);
  
}
