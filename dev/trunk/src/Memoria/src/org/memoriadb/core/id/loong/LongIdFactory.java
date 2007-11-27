package org.memoriadb.core.id.loong;

import java.io.*;

import org.memoriadb.core.id.*;
import org.memoriadb.core.util.Constants;

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
    fCurrentObjectId = Math.max(fCurrentObjectId, longValue.getLong()); 
  }

  @Override
  public IObjectId createFrom(DataInput input) throws IOException {
    long id = input.readLong();
    return new LongId(id);
  }

  @Override
  public IObjectId createNextId() {
    return new LongId(++fCurrentObjectId);
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
  public int getIdSize() {
    return Constants.LONG_LEN;
  }

  @Override
  public IObjectId getMemoriaClassDeletionMarker() {
    return MEMORIA_CLASS_DELETED;
  }

  @Override
  public IObjectId getFieldMetaClass() {
    return MEMORIA_META_CLASS_ID;
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

}
