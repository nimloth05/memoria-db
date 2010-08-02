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

package org.memoriadb.handler.collection;

import org.memoriadb.core.file.read.IReaderContext;
import org.memoriadb.handler.IBindable;

import java.util.Collection;

public class CollectionBindable implements IBindable {

  private final Collection<Object> fCollection;
  private final Object fValue;

  public CollectionBindable(Collection<Object> collection, Object value) {
    fCollection = collection;
    fValue = value;
  }

  @Override
  public void bind(IReaderContext context) throws Exception {
    fCollection.add(fValue);
  }

}
