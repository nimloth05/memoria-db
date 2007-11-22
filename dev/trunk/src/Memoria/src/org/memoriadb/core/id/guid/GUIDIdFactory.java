package org.memoriadb.core.id.guid;

import java.io.*;

import org.memoriadb.core.id.*;

public class GUIDIdFactory extends AbstractIdFactory implements IObjectIdFactory {
  
  private static final IObjectId MEMORIA_META_CLASS_ID = new GUIDObjectId("0476643a-da48-4381-b5e7-d8c5028e20d8");
  private static final IObjectId HANDLER_MEMORIA_CLASS_OBJECT_ID = new GUIDObjectId("edd0143e-6b74-4a27-8095-9f6c9c264008");
  private static final IObjectId ARRAY_MEMORIA_CLASS_ID = new GUIDObjectId("a49e712b-8c0d-4614-b1c1-67417f97bfdb");
  
  private static final IObjectId ROOT_CLASS_ID = new GUIDObjectId("4bb83f8c-f291-4479-9071-6616d7a60569");
  private static final IObjectId MEMORIA_CLASS_DELETED_ID = new GUIDObjectId("b629f86e-a9ce-49a2-94b9-e2be3ca5bc59");
  private static final IObjectId OBJECT_DELETED_ID = new GUIDObjectId("15915c65-7425-4cd4-bf93-4562fb188e61");
  private static final IObjectId NULL_REFERENCE_ID = new GUIDObjectId("5c66311d-69c6-4f87-92f9-f5a0b1177b06");
  private static final IObjectId PRIMITIVE_CLASS_ID = new GUIDObjectId("a1f42570-94db-4644-b925-a3a18190ed0b");

  @Override
  public void adjustId(IObjectId id) {}

  @Override
  public IObjectId createFrom(DataInput input) throws IOException {
    return GUIDObjectId.readFrom(input);
  }

  @Override
  public IObjectId createNextId() {
    return GUIDObjectId.random();
  }

  @Override
  public IObjectId getArrayMemoriaClass() {
    return ARRAY_MEMORIA_CLASS_ID;
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
  public IObjectId getMemoriaMetaClass() {
    return MEMORIA_META_CLASS_ID;
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
