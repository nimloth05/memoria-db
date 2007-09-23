package org.memoriadb.core.load.binder;

import java.lang.reflect.Array;

import org.memoriadb.core.load.*;
import org.memoriadb.exception.MemoriaException;

public class BindArray implements IBindable {
  
  private final long fId;
  private final Object fArray;
  private final int fIndex;

  public BindArray(Object array, int index, long objectId) {
    fIndex = index;
    if (!array.getClass().isArray()) throw new MemoriaException("Object is not an array");
    fArray = array;
    fId = objectId;
  }

  @Override
  public void bind(IReaderContext context) throws Exception {
      Object obj = context.getObjectById(fId);
      Array.set(fArray, fIndex, obj);
  }

}
