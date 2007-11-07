package org.memoriadb.core.id.def;

import java.io.*;

import org.memoriadb.core.id.*;

public class LongIdFactory implements IObjectIdFactory {
  
  private static final IObjectId MEMORIA_META_CLASS_ID =          new LongObjectId(1);
  private static final IObjectId HANDLER_MEMORIA_CLASS_OBJECT_ID = new LongObjectId(2);
  private static final IObjectId ARRAY_MEMORIA_CLASS =             new LongObjectId(3);
  
  private static final IObjectId ROOT_CLASS_ID =    new LongObjectId(-1);
  private static final IObjectId MEMORIA_CLASS_DELETED = new LongObjectId(-2);
  private static final IObjectId OBJECT_DELETED =    new LongObjectId(-3);
  
  private long fCurrentObjectId = 0;

  @Override
  public void adjustId(IObjectId id) {
    LongObjectId longValue = (LongObjectId) id;
    fCurrentObjectId = Math.max(fCurrentObjectId, longValue.getLong()); 
  }

  @Override
  public IObjectId createFrom(DataInput input) throws IOException {
    long id = input.readLong();
    return new LongObjectId(id);
  }

  @Override
  public IObjectId createNextId() {
    return new LongObjectId(++fCurrentObjectId);
  }

  @Override
  public IObjectId getArrayMemoriaClass() {
    return ARRAY_MEMORIA_CLASS;
  }

  @Override
  public IObjectId getHandlerMetaClass() {
    return HANDLER_MEMORIA_CLASS_OBJECT_ID;
  }

  @Override
  public IObjectId getMemoriaClassDeletionMarker() {
    return MEMORIA_CLASS_DELETED;
  }

  @Override
  public IObjectId getMemoriaMetaClass() {
    return MEMORIA_META_CLASS_ID;
  }

  @Override
  public IObjectId getObjectDeletionMarker() {
    return OBJECT_DELETED;
  }

  @Override
  public IObjectId getRootClassId() {
    return ROOT_CLASS_ID;
  }

  @Override
  public boolean isMemoriaClassDeletionMarker(IObjectId typeId) {
    return MEMORIA_CLASS_DELETED.equals(typeId);
  }

  @Override
  public boolean isMemoriaClass(IObjectId typeId) {
    return MEMORIA_META_CLASS_ID.equals(typeId);
  }

  @Override
  public boolean isObjectDeletionMarker(IObjectId typeId) {
    return OBJECT_DELETED.equals(typeId);
  }

  @Override
  public boolean isRootClassId(IObjectId superClassId) {
    return ROOT_CLASS_ID.equals(superClassId);
  }

}
