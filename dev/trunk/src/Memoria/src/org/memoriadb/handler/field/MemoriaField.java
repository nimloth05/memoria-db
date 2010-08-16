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

package org.memoriadb.handler.field;

import org.memoriadb.core.meta.Type;
import org.memoriadb.core.util.ReflectionUtil;

import java.lang.reflect.Field;


public final class MemoriaField {

  private final int fFieldId;
  private final String fName;
  private final Type fType;
  private final boolean fIsWeakRef;

  public static MemoriaField create(int id, Field field) {
    MemoriaField result = new MemoriaField(id, field.getName(), Type.getType(field), ReflectionUtil.hasWeakRefAnnotation(field));
    return result;
  }

  /**
   * @param id
   * @param name
   * @param type
   * @param isWeakRef
   */
  public MemoriaField(int id, String name, Type type, boolean isWeakRef) {
    fFieldId = id;
    fType = type;
    fName = name;
    fIsWeakRef = isWeakRef;
  }

  public Type getFieldType() {
    return fType;
  }

  public int getId() {
    return fFieldId;
  }

  public String getName() {
    return fName;
  }
  
  public Type getType() {
    return fType;
  }

  public boolean isWeakRef() {
    return fIsWeakRef;
  }

  @Override
  public String toString() {
    return "FieldName: " + fName;
  }

}
