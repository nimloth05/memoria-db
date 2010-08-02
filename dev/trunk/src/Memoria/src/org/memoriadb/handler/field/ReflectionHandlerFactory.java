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

import org.memoriadb.core.meta.IMemoriaClassConfig;
import org.memoriadb.core.meta.MemoriaClass;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.id.IObjectId;

/**
 * Helper Factory for memoriaClass and Handlers which are reflection based.
 *
 * Internal Class.
 */
public final class ReflectionHandlerFactory {
  /**
   * Introspects the given klass and adds all fields. Used to initially create a MetaClass, when the first object of a
   * given type enters the memoria-reference-space.
   *
   * @param klass the java-type for the new memoria Class.
   * @param memoriaClassId id of the memoria-type of this class.
   *
   * @return
   */
  public static IMemoriaClassConfig createNewType(Class<?> klass, IObjectId memoriaClassId) {
    return createNewType(klass, memoriaClassId, ReflectionUtil.hasValueObjectAnnotation(klass));
  }
 /**
   * Introspects the given klass and adds all fields. Used to initially create a MetaClass, when the first object of a
   * given type enters the memoria-reference-space.
   *
   * @param klass java type of the the new handler and memoriaClass.
   * @param memoriaClassId id of the memoriaClass which this class can be serialized.
   * @param isValueObject <tt>true</tt> if this class represent a valueObject.
   *
  * @return
  */
  public static IMemoriaClassConfig createNewType(Class<?> klass, IObjectId memoriaClassId, boolean isValueObject) {
    FieldbasedObjectHandler handler = FieldbasedObjectHandler.createNewType(klass);
    return new MemoriaClass(handler, memoriaClassId, isValueObject);
  }
}
