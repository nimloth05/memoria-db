package org.memoriadb.core.file;

import org.memoriadb.core.DBMode;
import org.memoriadb.core.id.IObjectId;

public interface ISerializeContext {
  
  public DBMode getDBMode();

  public IObjectId getObjectId(Object obj);
  
  public IObjectId getRootClassId();

}
