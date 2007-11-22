package org.memoriadb.core.handler.array;

import java.lang.reflect.Array;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.IMemoriaClassConfig;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.ReflectionUtil;

/**
 * Implementation for class-mode
 * 
 * @author msc
 */
public class ObjectArray implements IArray {

  private final Object fArray;

  public ObjectArray(IObjectId memoriaClassId, IMemoriaClassConfig memoriaClass, int dimension, int length) {
    int[] dimensions = new int[dimension];
    // The first int gives the size of the array created here.
    dimensions[0] = length;

    fArray = Array.newInstance(ReflectionUtil.getClass(memoriaClass.getJavaClassName()), dimensions);
  }

  public ObjectArray(Object array) {
    if (!array.getClass().isArray()) throw new MemoriaException("Object is not an array: " + array);
    fArray = array;
  }

  @Override
  public Object get(int index) {
    return Array.get(fArray, index);
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return null;
  }

  @Override
  public Object getResult() {
    return fArray;
  }

  @Override
  public int length() {
    return Array.getLength(fArray);
  }

  @Override
  public void set(int index, Object value) {
    Array.set(fArray, index, value);
  }

}
