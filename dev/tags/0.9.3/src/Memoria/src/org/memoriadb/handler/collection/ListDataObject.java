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

import org.memoriadb.id.IObjectId;

import java.util.Collection;
import java.util.List;

/**
 * @author Sandro
 */
public class ListDataObject implements IListDataObject {
  
  private final IObjectId fId;
  private final List<Object> fList;

  public ListDataObject(List<Object> list, IObjectId id) {
    fList = list;
    fId = id;
  }

  @Override
  public Collection<Object> getCollection() {
    return fList;
  }

  @Override
  public List<Object> getList() {
    return fList;
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fId;
  }


  @Override
  public String toString() {
    return fList.toString();
  }

}
