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
import org.memoriadb.id.IObjectId;

/**
 * TypeInformation API is not stable, the getters returns null - which is not desirable.
 * 
 * @author Sandro
 *
 */
public interface ITypeInfo {

  /**
   * Adds a MemoriaClass (if not already present) for the given java class.  
   * 
   * @param clazz
   * @return Id for the memoria class
   * @param clazz
   */
  public IObjectId addMemoriaClassIfNecessary(Class<?> clazz);

  public Iterable<IMemoriaClass> getAllClasses();
  
  public IObjectId getMemoriaArrayClass();
  
  /**
   * @param clazz
   * @return The MemoriaClass for the given <tt>clazz</tt> or null.
   * @param clazz
   */
  public IMemoriaClass getMemoriaClass(Class<?> clazz);
  
  /**
   * @param object
   * @return The MemoriaClass for the given <tt>obj</tt> or null.
   * @param object
   */
  public IMemoriaClass getMemoriaClass(Object object);

  /**
   * @param className
   * @return The MemoriaClass for the given <tt>className</tt> or null.
   * @param className
   */
  public IMemoriaClass getMemoriaClass(String className);
  
  public IObjectId getMemoriaClassId(Class<?> clazz);
  
  public IObjectId getMemoriaClassId(Object object);
  
  public IObjectId getMemoriaClassId(String className);
}
