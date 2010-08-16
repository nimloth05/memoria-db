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

package org.memoriadb.core.file;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.id.IObjectId;

/**
 * @author Sandro
 */
public interface IWriterContext {
  
  public boolean contains(Object obj);

  /**
   * @param object
   * @return The id for the given <tt>obj</tt>
   * @throw {@link MemoriaException} if the given <tt>obj</tt> is not found.
   * @param object
   */
  public IObjectId getExistingtId(Object object);
  
  public IMemoriaClass getMemoriaClass(IObjectId id);
  
  /**
   * 
   * @param object
   * @return the memoriaClass for the given object.
   */
  public IMemoriaClass getMemoriaClass(Object object);

  /**
   * @param javaClassName
   * @return ObjectId of the MemoriaClass representing the given java-class
   * @throws MemoriaException if no class is found.
   * @param javaClassName
   */
  public IObjectId getMemoriaClassId(String javaClassName);
  
  public IObjectId getNullReference();

  public IObjectId getRootClassId();

}
