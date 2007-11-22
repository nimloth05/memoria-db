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
  private final IObjectId fComponentTypeId;
  private final TypeInfo fTypeInfo;

  /**
   * @param arrayClassId
   *          id of the generic array class
   * @param componentTypeId
   *          id of the component-type, {@link IDefaultObjectIdProvider#getPrimitiveClassId()} if the component type is
   *          a primitive (string, int, Integer etc)
   * 
   * @param dimension
   * @param length
   */
  public ObjectArray(IObjectId arrayClassId, IObjectId componentTypeId, TypeInfo typeInfo, int length) {
    super(arrayClassId);
    fComponentTypeId = componentTypeId;
    fTypeInfo = typeInfo;

    int[] dimensions = new int[typeInfo.getDimension()];
    // The first int gives the size of the array created here.
    dimensions[0] = length;

    fArray = Array.newInstance(typeInfo.getJavaClass(), dimensions);
  }

  public ObjectArray(Object array) {
    // type-info never used.....
    super(null);
    fComponentTypeId = null;

    if (!array.getClass().isArray()) throw new MemoriaException("Object is not an array: " + array);
    fArray = array;
    fTypeInfo = ReflectionUtil.getComponentTypeInfo(array.getClass());
  }

  @Override
  public Object get(int index) {
    return Array.get(fArray, index);
  }

  @Override
  public IObjectId getComponentTypeId() {
    return fComponentTypeId;
  }

  @Override
  public TypeInfo getComponentTypeInfo() {
    return fTypeInfo;
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
