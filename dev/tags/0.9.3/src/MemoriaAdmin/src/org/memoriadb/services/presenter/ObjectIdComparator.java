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

package org.memoriadb.services.presenter;

import org.memoriadb.id.IIntegralId;
import org.memoriadb.id.IObjectId;
import org.memoriadb.id.loong.LongId;

import java.util.Comparator;

public final class ObjectIdComparator implements Comparator<IObjectId> {
  
  @Override
  public int compare(IObjectId o1, IObjectId o2) {
    if (o1 instanceof IIntegralId && o2 instanceof IIntegralId) {
      LongId id1 = (LongId) o1;
      LongId id2 = (LongId)o2;
      return id1.getValue() > id2.getValue() ? 1 : (id1.getValue() < id2.getValue() ? -1 : 0);
    }
    
    return o1.toString().compareTo(o2.toString());
  }
  
}