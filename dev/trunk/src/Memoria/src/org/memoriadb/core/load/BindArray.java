package org.memoriadb.core.load;

import java.lang.reflect.Array;

import org.memoriadb.exception.MemoriaException;

public class BindArray implements IBindable {
  
  private final long[] fIds;
  private final Object fArray;

  public BindArray(Object array, long[] ids) {
    if (!array.getClass().isArray()) throw new MemoriaException("Object is not an array");
    if (Array.getLength(array) != ids.length) throw new MemoriaException("id-array and target array has not the same length.");
    fArray = array;
    fIds = ids;
  }

  @Override
  public void bind(IReaderContext contex) throws Exception {
    for(int index  = 0; index < fIds.length; ++index) {
      Object obj = contex.getObjectById(fIds[index]);
      Array.set(fArray, index, obj);
    }
  }

}
