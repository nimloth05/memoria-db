package org.memoriadb.core.file;

import org.memoriadb.core.id.IObjectId;

public interface ISerializeContext {
  
  public IObjectId getObjectId(Object obj);

  public IObjectId getRootClassId();

}
