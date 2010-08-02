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

package org.memoriadb.handler.array;

import org.memoriadb.id.IObjectId;

public abstract class AbstractArray implements IArray {
  
  private final IObjectId fArrayClassId;

  /**
   * @param arrayClassId id of the generic array class
   */
  public AbstractArray(IObjectId arrayClassId) {
    fArrayClassId = arrayClassId;
  }
  
  @Override
  public IObjectId getMemoriaClassId() {
    return fArrayClassId;
  }
  
}
