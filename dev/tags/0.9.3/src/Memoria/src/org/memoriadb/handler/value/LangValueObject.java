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

package org.memoriadb.handler.value;

import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.IObjectId;

/**
 * ValueObject for the java language primitive Value-Objects.
 * 
 * @author Sandro
 *
 * @param <T> Type of the primitive
 */
public class LangValueObject<T> implements IDataObject {

  private T fObject;
  private final IObjectId fClassId;
  
  public LangValueObject(T object, IObjectId classId) {
    fObject = object;
    fClassId = classId;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final LangValueObject<?> other = (LangValueObject<?>) obj;
    if (fClassId == null) {
      if (other.fClassId != null) return false;
    }
    else if (!fClassId.equals(other.fClassId)) return false;
    if (fObject == null) {
      if (other.fObject != null) return false;
    }
    else if (!fObject.equals(other.fObject)) return false;
    return true;
  }
  
  public T get() {
    return fObject;
  }
  
  @Override
  public IObjectId getMemoriaClassId() {
    return fClassId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fClassId == null) ? 0 : fClassId.hashCode());
    result = prime * result + ((fObject == null) ? 0 : fObject.hashCode());
    return result;
  }

  public void set(T t) {
    fObject = t;
  }

  @Override
  public String toString() {
    return String.valueOf(fObject);
  }
}
