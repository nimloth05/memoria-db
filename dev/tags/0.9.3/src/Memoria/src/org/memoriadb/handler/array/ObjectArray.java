/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.handler.array;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.util.ArrayTypeInfo;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.id.IIdProvider;
import org.memoriadb.id.IObjectId;

import java.lang.reflect.Array;

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
   *          id of the component-type, {@link IIdProvider#getPrimitiveClassId()} if the component type is
   *          a primitive (string, int, Integer etc)
   * 
   * @param componentTypeInfo
   * @param componentTypeInfo
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
  public ArrayTypeInfo getTypeInfo() {
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
