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

package org.memoriadb.id.guid;

import org.memoriadb.core.util.io.IDataInput;
import org.memoriadb.id.AbstractIdFactory;
import org.memoriadb.id.IObjectId;
import org.memoriadb.id.IObjectIdFactory;

import java.io.IOException;

public class GuidIdFactory extends AbstractIdFactory implements IObjectIdFactory {
  
  private static final IObjectId MEMORIA_META_CLASS_ID = GuidId.fromString("0476643a-da48-4381-b5e7-d8c5028e20d8");
  private static final IObjectId HANDLER_MEMORIA_CLASS_OBJECT_ID = GuidId.fromString("edd0143e-6b74-4a27-8095-9f6c9c264008");
  private static final IObjectId ARRAY_MEMORIA_CLASS_ID = GuidId.fromString("a49e712b-8c0d-4614-b1c1-67417f97bfdb");
  
  private static final IObjectId ROOT_CLASS_ID = GuidId.fromString("4bb83f8c-f291-4479-9071-6616d7a60569");
  private static final IObjectId MEMORIA_CLASS_DELETED_ID = GuidId.fromString("b629f86e-a9ce-49a2-94b9-e2be3ca5bc59");
  private static final IObjectId OBJECT_DELETED_ID  = GuidId.fromString("15915c65-7425-4cd4-bf93-4562fb188e61");
  private static final IObjectId NULL_REFERENCE_ID  = GuidId.fromString("5c66311d-69c6-4f87-92f9-f5a0b1177b06");
  private static final IObjectId PRIMITIVE_CLASS_ID = GuidId.fromString("a1f42570-94db-4644-b925-a3a18190ed0b");

  @Override
  public void adjustId(IObjectId id) {}

  @Override
  public IObjectId createFrom(IDataInput input) throws IOException {
    return GuidId.readFrom(input);
  }

  @Override
  public IObjectId createNextId() {
    return GuidId.random();
  }

  @Override
  public IObjectId fromString(String string) {
    return GuidId.fromString(string);
  }

  @Override
  public IObjectId getArrayMemoriaClass() {
    return ARRAY_MEMORIA_CLASS_ID;
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
    return 16;
  }

  @Override
  public IObjectId getMemoriaClassDeletionMarker() {
    return MEMORIA_CLASS_DELETED_ID;
  }

  @Override
  public IObjectId getNullReference() {
    return NULL_REFERENCE_ID;
  }

  @Override
  public IObjectId getObjectDeletionMarker() {
    return OBJECT_DELETED_ID;
  }

  @Override
  public IObjectId getPrimitiveClassId() {
    return PRIMITIVE_CLASS_ID;
  }

  @Override
  public IObjectId getRootClassId() {
    return ROOT_CLASS_ID;
  }

}
