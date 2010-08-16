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

package org.memoriadb;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.array.IArray;
import org.memoriadb.handler.enu.IEnumObject;
import org.memoriadb.handler.field.IFieldbasedObject;
import org.memoriadb.handler.value.LangValueObject;

/**
 * @author Sandro
 */
public interface IRefactor {

  /**
   * Creates 
   * @param object TODO
   * @return
   */
  public IFieldbasedObject asFieldDataObject(Object object);
  
  /**
   * 
   * @param klass type of the array: int[].class, int[][].class, etc.
   * @param length
   * @return
   */
  public IArray createArray(Class<?> klass, int length);

  public IArray createArray(String componentType, int dimension, int length);

  /**
   * @param className
   * @param className
   * @param name
   * @param name
   * @return the wrapped enum-object, if it is found in the repo. else, a new EnumDataObject
   * is created which is not yet saved.
   */
  public IEnumObject getEnum(String className, String name);
  
  public <T> LangValueObject<T> getLangValueObject(T value);

  /**
   * 
   * @param string
   * @param string2
   * @return
   */
  public IMemoriaClass renameClass(String string, String string2);

}
