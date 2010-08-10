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

package org.memoriadb.core.file.read;

import org.memoriadb.core.util.io.IDataInput;
import org.memoriadb.handler.IBindable;
import org.memoriadb.id.IObjectId;
import org.memoriadb.instantiator.IInstantiator;

import java.io.IOException;


public interface IReaderContext {

  /**
   * Use this method for bindings with NO side-effects  (no method-call during binding): for example fields, arrays.
   * @param bindable
   * @param bindable
   */
  public void addGenOneBinding(IBindable bindable);

  /**
   * Use this method for bindings with side-effects (for example call to equals, hashCode): Sets, maps.
   * 
   * The bindings added with this method are added after the bindings added with {@link IReaderContext#addGenOneBinding(IBindable)}.
   * @param bindable
   * @param bindable
   */
  public void addGenTwoBinding(IBindable bindable);
  
  public IObjectId getArrayMemoriaClass();
  
  public IInstantiator getDefaultInstantiator();

  /**
   * @param id
   * @return the object for the given <tt>id</tt>
   * @throws {@link MemoriaException} if the given <tt>id</tt> is not found.
   * @param id
   */
  public Object getExistingObject(IObjectId id);
  
  /**
   * @param id
   * @return the object for the given <tt>id</tt> or null.
   * @param id
   */
  public Object getObject(IObjectId id);

  public IObjectId getPrimitiveClassId();
  
  public boolean isInDataMode();
  
  public boolean isNullReference(IObjectId id);
  
  public boolean isRootClassId(IObjectId superClassId);

  public IObjectId readObjectId(IDataInput input) throws IOException;

}
