package org.memoriadb.core.id;

import java.io.DataInput;

public interface IObjectIdFactory {
  
  
  public IObjectId createFrom(DataInput input);

  public IObjectId createNextId();

  public IObjectId getArrayMemoriaClass();

  /**
   * ObjectID for the Meta-MetaClass for the field based MetaClass.
   */
  public IObjectId getMemoriaMetaClass();

  public boolean isMemoriaClassDeleted(IObjectId typeId);

  public boolean isMemoriaMetaClass(IObjectId typeId);

  public boolean isObjectDeleted(IObjectId typeId);

}
