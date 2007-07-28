package org.memoriadb.core;

import java.lang.reflect.Field;

public interface IReaderContext {

  public Object getObjectById(long objectId);

  public void objectToBind(Object object, Field field, long targetId);

}
