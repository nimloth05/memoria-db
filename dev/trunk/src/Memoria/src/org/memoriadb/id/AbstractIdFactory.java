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


public abstract class AbstractIdFactory implements IObjectIdFactory {

  @Override
  public boolean isMemoriaClassDeletionMarker(IObjectId typeId) {
    return getMemoriaClassDeletionMarker().equals(typeId);
  }
  
  @Override
  public boolean isMemoriaFieldClass(IObjectId typeId) {
    return getFieldMetaClass().equals(typeId);
  }

  @Override
  public boolean isMemoriaHandlerClass(IObjectId typeId) {
    return getHandlerMetaClass().equals(typeId);
  }

  @Override
  public boolean isNullReference(IObjectId objectId) {
    return getNullReference().equals(objectId);
  }

  @Override
  public boolean isObjectDeletionMarker(IObjectId typeId) {
    return getObjectDeletionMarker().equals(typeId);
  }

  @Override
  public boolean isRootClassId(IObjectId superClassId) {
    return getRootClassId().equals(superClassId);
  }

}
