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

package org.memoriadb.handler.array;

import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.handler.IBindable;
import org.memoriadb.id.IObjectId;

/**
 * @author Sandro
 */
public class BindArray implements IBindable {
  
  private final IObjectId fId;
  private final IArray fArray;
  private final int fIndex;

  public BindArray(IArray array, int index, IObjectId objectId) {
    fIndex = index;
    fArray = array;
    fId = objectId;
  }

  @Override
  public void bind(IReaderContext context) throws Exception {
      Object obj = context.isNullReference(fId)? null: context.getExistingObject(fId);
      fArray.set(fIndex, obj);
  }

}
