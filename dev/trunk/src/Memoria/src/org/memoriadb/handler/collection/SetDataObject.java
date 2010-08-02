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

import org.memoriadb.id.IObjectId;

import java.util.Collection;
import java.util.Set;

public class SetDataObject implements ISetDataObject {

  private final IObjectId fId;
  private final Set<Object> fSet;

  public SetDataObject(Set<Object> list, IObjectId id) {
    fSet = list;
    fId = id;
  }

  @Override
  public Collection<Object> getCollection() {
    return fSet;
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fId;
  }

  @Override
  public Set<Object> getSet() {
    return fSet;
  }
  
  @Override
  public String toString() {
    return fSet.toString();
  }

}
