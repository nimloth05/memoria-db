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
import org.memoriadb.id.IObjectId;



/**
 * 
 * Visits all direct children of the given object to check for enums.
 * 
 * @author Sandro
 *
 */
public class SaveTraversal implements IObjectTraversal {

  private final TransactionHandler fTransactionHandler;
  
  public SaveTraversal(TransactionHandler transactionHandler) {
    fTransactionHandler = transactionHandler;
  }

  @Override
  public void handle(Object obj) {
    IObjectId memoriaClassId = fTransactionHandler.addMemoriaClassIfNecessary(obj);
    fTransactionHandler.internalSave(obj, memoriaClassId);
    fTransactionHandler.getMemoriaClass(obj).getHandler().traverseChildren(obj, new IObjectTraversal() {

      @Override
      public void handle(Object object) {
        IObjectId classId = fTransactionHandler.addMemoriaClassIfNecessary(object);
        IMemoriaClass clazz = fTransactionHandler.getObject(classId);
        if(clazz.isValueObject()) new AddMemoriaClassesTraversal(fTransactionHandler).handle(object);
      }
      
    });
  }

}
