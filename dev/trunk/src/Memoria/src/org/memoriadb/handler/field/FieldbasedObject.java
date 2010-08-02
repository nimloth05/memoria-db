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

package org.memoriadb.handler.field;

import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.id.IObjectId;


public class FieldbasedObject implements IFieldbasedObject {

  private final Object fObejct;
  private IObjectId fMemoriaClassId;

  public FieldbasedObject(Object object) {
    fObejct = object;
  }
  
  public FieldbasedObject(Object object, IObjectId memoriaClassId) {
    fObejct = object;
    fMemoriaClassId = memoriaClassId;
  }
  
  @Override
  public boolean equalsLangValueObject(String fieldName, Object value) {
    return get(fieldName).equals(value);
  }

  @Override
  public Object get(String fieldName) {
    return ReflectionUtil.getValueFromField(fObejct, fieldName);
  }

  @Override
  public IObjectId getMemoriaClassId() {
    if(fMemoriaClassId == null) throw new UnsupportedOperationException();
    return fMemoriaClassId;
  }

  @Override
  public Object getObject() {
    return fObejct;
  }

  @Override
  public void set(String fieldName, Object value) {
    ReflectionUtil.setValueForField(fObejct, fieldName, value); 
  }

}
