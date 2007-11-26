package org.memoriadb.core.handler.array;

import java.lang.reflect.Array;

import org.memoriadb.core.id.*;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.*;

/**
 * Implementation for class-mode
 * 
 * @author msc
 */
public class ObjectArray extends AbstractArray {

  private final Object fArray;
  private final ArrayTypeInfo fComponentTypeInfo;

  /**
   * @param arrayClassId
   *          id of the generic array class
   * @param componentTypeId
   *          id of the component-type, {@link IDefaultIdProvider#getPrimitiveClassId()} if the component type is
   *          a primitive (string, int, Integer etc)
   * 
   * @param dimension
   * @param length
   */
  public ObjectArray(IObjectId arrayClassId, ArrayTypeInfo componentTypeInfo, int length) {
    super(arrayClassId);
    fComponentTypeInfo = componentTypeInfo;

    int[] dimensions = new int[componentTypeInfo.getDimension()];
    // The first int gives the size of the array created here.
    dimensions[0] = length;

    fArray = Array.newInstance(componentTypeInfo.getJavaClass(), dimensions);
  }

  public ObjectArray(Object array) {
    // type-info never used.....
    super(null);

    if (!array.getClass().isArray()) throw new MemoriaException("Object is not an array: " + array);
    fArray = array;
    fComponentTypeInfo = ReflectionUtil.getComponentTypeInfo(array.getClass());
  }

  @Override
  public Object get(int index) {
    return Array.get(fArray, index);
  }

  @Override
  public ArrayTypeInfo getComponentTypeInfo() {
    return fComponentTypeInfo;
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
