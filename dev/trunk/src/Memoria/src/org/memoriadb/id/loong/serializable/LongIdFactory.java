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

package org.memoriadb.id.loong.serializable;

import org.memoriadb.core.util.Constants;
import org.memoriadb.core.util.io.IDataInput;
import org.memoriadb.id.AbstractIdFactory;
import org.memoriadb.id.IObjectId;
import org.memoriadb.id.IObjectIdFactory;

import java.io.IOException;

public class LongIdFactory extends AbstractIdFactory implements IObjectIdFactory {
  
  private static final IObjectId MEMORIA_META_CLASS_ID =          new LongId(1);
  private static final IObjectId HANDLER_MEMORIA_CLASS_OBJECT_ID = new LongId(2);
  private static final IObjectId ARRAY_MEMORIA_CLASS =             new LongId(3);
  
  private static final IObjectId ROOT_CLASS_ID =    new LongId(-1);
  private static final IObjectId MEMORIA_CLASS_DELETED = new LongId(-2);
  private static final IObjectId OBJECT_DELETED =    new LongId(-3);
  private static final IObjectId NULL_REFERENCE_ID =    new LongId(-4);
  private static final IObjectId PRIMITIVE_CLASS_ID =    new LongId(-5);
  
  private long fCurrentObjectId = 0;

  @Override
  public void adjustId(IObjectId id) {
    LongId longValue = (LongId) id;
    fCurrentObjectId = Math.max(fCurrentObjectId, longValue.getValue()); 
  }

  @Override
  public IObjectId createFrom(IDataInput input) throws IOException {
    long id = input.readLong();
    return new LongId(id);
  }

  @Override
  public IObjectId createNextId() {
    return new LongId(++fCurrentObjectId);
  }

  @Override
  public IObjectId fromString(String string) {
    return new LongId(Long.parseLong(string));
  }

  @Override
  public IObjectId getArrayMemoriaClass() {
    return ARRAY_MEMORIA_CLASS;
  }

  @Override
  public IObjectId getFieldMetaClass() {
    return MEMORIA_META_CLASS_ID;
  }

  @Override
  public IObjectId getHandlerMetaClass() {
    return HANDLER_MEMORIA_CLASS_OBJECT_ID;
  }

  @Override
  public int getIdSize() {
    return Constants.LONG_LEN;
  }

  @Override
  public IObjectId getMemoriaClassDeletionMarker() {
    return MEMORIA_CLASS_DELETED;
  }

  @Override
  public IObjectId getNullReference() {
    return NULL_REFERENCE_ID;
  }

  @Override
  public IObjectId getObjectDeletionMarker() {
    return OBJECT_DELETED;
  }

  @Override
  public IObjectId getPrimitiveClassId() {
    return PRIMITIVE_CLASS_ID;
  }

  @Override
  public IObjectId getRootClassId() {
    return ROOT_CLASS_ID;
  }

  public LongId peekNexId() {
    return new LongId(fCurrentObjectId+1);
  }

}
