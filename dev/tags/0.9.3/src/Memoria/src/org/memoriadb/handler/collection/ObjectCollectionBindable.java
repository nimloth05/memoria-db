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

package org.memoriadb.handler.collection;

import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.handler.IBindable;
import org.memoriadb.id.IObjectId;

import java.util.Collection;

/**
 * @author Sandro
 */
public class ObjectCollectionBindable implements IBindable {
  
  private final Collection<Object> fResult;
  private final IObjectId fObjectId;

  public ObjectCollectionBindable(Collection<Object> result, IObjectId objectId) {
    fResult = result;
    fObjectId = objectId;
  }

  @Override
  public void bind(IReaderContext context) throws Exception {
    Object object = context.getExistingObject(fObjectId);
    fResult.add(object);
  }

  @Override
  public String toString() {
    return "list: " + fResult + " object to add: " + fObjectId;
  }

}
