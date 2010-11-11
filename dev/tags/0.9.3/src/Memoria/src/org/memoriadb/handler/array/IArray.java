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

package org.memoriadb.handler.array;

import org.memoriadb.core.util.ArrayTypeInfo;
import org.memoriadb.handler.IDataObject;

/**
 * Represents the array in a mode-independant way.
 * 
 * @author msc
 *
 */
public interface IArray extends IDataObject {
  
  /**
   * @return the value at the given <tt>index</tt>-position
   */
  public Object get(int index);

  public ArrayTypeInfo getTypeInfo();

  /**
   * @return the array-object, depending on the mode either a java-Array or an {@link org.memoriadb.handler.array.DataArray}.
   */
  public Object getResult();
  
  public int length();
  
  /**
   * Sets a value
   */
  public void set(int index, Object value);
  
}
