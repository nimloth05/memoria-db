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

import org.memoriadb.handler.value.LangValueObject;
import org.memoriadb.id.IObjectId;

import java.util.HashMap;
import java.util.Map;

public class FieldbasedDataObject implements IFieldbasedObject {
  
  private final Map<String, Object> fData = new HashMap<String, Object>();
  private final IObjectId fMemoriaClassId;
  
  public FieldbasedDataObject(IObjectId memoriaClassId) {
    fMemoriaClassId = memoriaClassId;
  }

  @Override
  public boolean equalsLangValueObject(String fieldName, Object value) {
    return ((LangValueObject<?>)get(fieldName)).get().equals(value);
  }

  @Override
  public Object get(String string) {
    return fData.get(string);
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fMemoriaClassId;
  }

  @Override
  public Object getObject() {
    return this;
  }

  @Override
  public void set(String fieldName, Object value) {
    fData.put(fieldName, value);
  }

}
