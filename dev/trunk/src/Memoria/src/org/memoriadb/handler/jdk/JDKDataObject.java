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

package org.memoriadb.handler.jdk;

import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.IObjectId;

public class JDKDataObject<T> implements IDataObject {
  
  private T fObject;
  private final IObjectId fClassId;
  
  public static <E> JDKDataObject<E> create(IObjectId classId, E object) {
    return new JDKDataObject<E>(classId, object);
  }
  
  private JDKDataObject(IObjectId classId, T object) {
    fClassId = classId;
    fObject = object;
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fClassId;
  }
  
  public T getObject() {
    return fObject;
  }
  
  public void setObject(T object) {
    fObject = object;
  }
  
  @Override
  public String toString() {
    return fObject.toString();
  }

}
