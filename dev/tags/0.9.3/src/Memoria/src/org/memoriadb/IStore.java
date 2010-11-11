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

import org.memoriadb.core.IObjectInfo;
import org.memoriadb.id.IObjectId;

public interface IStore {
  
  /**
   * Starts an update. Changes are immediately refelcted in memory, but not written back to the
   * persistent store until <tt>endUpdate()</tt> is called.
   */
  public void beginUpdate();
  
  /**
   * Closes this ObjectStore permanently. Open FileHandles are also closed. 
   * After calling close() this ObjectStore holds no locks in the FS.
   */
  public void close();
  
  public boolean containsId(IObjectId id);
  
  /**
   * Commits the changes since the last call to <tt>beginUpdate</tt>. 
   * Updates can be nested, what increases the update-counter. Changes are only written to the
   * persistent store if the update-counter is 0. 
   */
  public void endUpdate();

  /**
   * @return The head revision of this database. Is incremented after each transaction.
   */
  public long getHeadRevision();
  
  public IObjectInfo getObjectInfo(Object object);
  
  /**
   * Clients which need information about the stored java class hierarchy or want to add new memoria
   * classes should work with ghe returned ITypeInfo interface.
   * @return
   */
  public ITypeInfo getTypeInfo();
  
  /**
   * @return true, if the update-counter is > 0.  
   */
  public boolean isInUpdateMode();
  
}
