package org.memoriadb.core.id.def;

import java.io.DataInput;

import org.memoriadb.core.id.*;

public class LongIdFactory implements IObjectIdFactory {
  
  private static final IObjectId MEMORIA_META_CLASS_ID =          new LongObjectId(1);
  private static final IObjectId HANDLER_META_CLASS_OBJECT_ID = new LongObjectId(2);
  private static final IObjectId ARRAY_MEMORIA_CLASS =             new LongObjectId(3);
  
  private static final IObjectId NO_SUPER_CLASS =    new LongObjectId(-1);
  private static final IObjectId MEMORIA_CLASS_DELETED = new LongObjectId(-2);
  private static final IObjectId OBJECT_DELETED =    new LongObjectId(-3);
  
  private long fCurrentObjectId = 0;

  @Override
  public IObjectId createFrom(DataInput input) {
    long id = input.readLong();
    fCurrentObjectId = Math.max(fCurrentObjectId, id);
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
  public IObjectId getMemoriaMetaClass() {
    return MEMORIA_META_CLASS_ID;
  }

  @Override
  public boolean isMemoriaClassDeleted(IObjectId typeId) {
    return MEMORIA_CLASS_DELETED.equals(typeId);
  }

  @Override
  public boolean isMemoriaMetaClass(IObjectId typeId) {
    return MEMORIA_META_CLASS_ID.equals(typeId);
  }

  @Override
  public boolean isObjectDeleted(IObjectId typeId) {
    return OBJECT_DELETED.equals(typeId);
  }

}
