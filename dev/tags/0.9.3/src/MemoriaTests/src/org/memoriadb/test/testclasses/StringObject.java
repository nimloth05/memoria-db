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

package org.memoriadb.test.testclasses;

import org.memoriadb.core.mode.DataStore;
import org.memoriadb.handler.field.FieldbasedDataObject;
import org.memoriadb.handler.field.IFieldbasedObject;
import org.memoriadb.handler.value.LangValueObject;
import org.memoriadb.id.IObjectId;

public class StringObject implements Comparable<StringObject> {

  private String fString;

  public static IFieldbasedObject createFieldObject(DataStore dataStore, String stringValue) {
    IObjectId memoriaClassId = dataStore.getId(dataStore.getTypeInfo().getMemoriaClass(StringObject.class));
    return setField(dataStore.getRefactorApi().getLangValueObject(stringValue), memoriaClassId);
  }

  private static IFieldbasedObject setField(LangValueObject<String> stringValue, IObjectId memoriaClassId) {
    IFieldbasedObject result = new FieldbasedDataObject(memoriaClassId);
    result.set("fString", stringValue);
    return result;
  }

  public StringObject() {}

  public StringObject(String string) {
    fString = string;
  }

  @Override
  public int compareTo(StringObject o) {
    return this.fString.compareTo(o.fString);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final StringObject other = (StringObject) obj;
    if (fString == null) {
      if (other.fString != null) return false;
    }
    else if (!fString.equals(other.fString)) return false;
    return true;
  }

  public String getString() {
    return fString;
  }

  public void setString(String string) {
    fString = string;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fString == null) ? 0 : fString.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "s: " + fString;
  }

}
