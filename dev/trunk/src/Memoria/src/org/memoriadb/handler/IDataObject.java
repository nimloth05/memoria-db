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

package org.memoriadb.handler;

import org.memoriadb.id.IObjectId;

/**
 * This Interface will returned if you perform a query in DBMode.data.
 * 
 * When adding data in DBMode.data, you have to use a IDataObject. The handler to serialize the object
 * is chosen by the MemoriaClass and must be able to handle the given subclass of IDataObject.
 * 
 * @author sandro
 *
 */
public interface IDataObject {
  
  /**
   * 
   * @return ObjectId of the memoriaClass for this Object.
   */
  public IObjectId getMemoriaClassId();

}
