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

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.util.collection.identity.IdentityHashSet;

import java.util.Set;

/**
 * Traverses a given object graph and saves all visited objects
 * 
 * @author msc
 */
public class DeleteTraversal implements IObjectTraversal {

  private final Set<Object> fVisited = IdentityHashSet.create();
  private final TransactionHandler fTransactionHandler;
  
  public DeleteTraversal(TransactionHandler transactionHandler) {
    fTransactionHandler = transactionHandler;
  }

  @Override
  public void handle(Object obj) {
    if(fVisited.contains(obj)) return;
    fVisited.add(obj);

    if(fTransactionHandler.isEnum(obj)) return;
    
    IMemoriaClass memoriaClass = fTransactionHandler.getMemoriaClass(obj);
    if(memoriaClass == null) return;
    if(memoriaClass.isValueObject()) return;
    
    memoriaClass.getHandler().traverseChildren(obj, this);
    fTransactionHandler.internalDelete(obj);
  }

}
