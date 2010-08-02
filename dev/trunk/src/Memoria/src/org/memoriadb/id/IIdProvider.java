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

package org.memoriadb.id;


public interface IIdProvider {

  public IObjectId fromString(String string);

  public IObjectId getArrayMemoriaClass();
  
  /**
   * @return ObjectID of the MetaClass for field memoria classes.
   */
  public IObjectId getFieldMetaClass();

  /**
   * @return ObjectID of the MetaClass for handler memoria classes.
   */
  public IObjectId getHandlerMetaClass();
  
  /**
   * @return The number of bytes a IObjectId from thsi factory requires. The size must be equal for all ids.
   */
  public int getIdSize();
  
  public IObjectId getMemoriaClassDeletionMarker();
  
  public IObjectId getNullReference();
  
  public IObjectId getObjectDeletionMarker();

  /**
   * @return Id of primitives (String, Integer, int, etc.). No MemoriaClass is created, 
   * the returned id is never contained in the store.
   */
  public IObjectId getPrimitiveClassId();
  
  public IObjectId getRootClassId();
  
  public boolean isMemoriaClassDeletionMarker(IObjectId typeId);
  
  public boolean isMemoriaFieldClass(IObjectId typeId);
  
  public boolean isMemoriaHandlerClass(IObjectId typeId);
  
  public boolean isNullReference(IObjectId objectId);
  
  public boolean isObjectDeletionMarker(IObjectId typeId);
  
  public boolean isRootClassId(IObjectId superClassId);

}