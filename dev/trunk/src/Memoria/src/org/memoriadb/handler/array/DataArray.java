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

import org.memoriadb.core.util.ArrayTypeInfo;
import org.memoriadb.id.IObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for data-mode
 * 
 * @author msc
 *
 */
public class DataArray extends AbstractArray implements IDataArray {

  private final List<Object> fData = new ArrayList<Object>();
  private final ArrayTypeInfo fComponentTypeInfo;
  private int fLength;
  
  public DataArray(IObjectId arrayMemoriaClass, ArrayTypeInfo componentTypeInfo, int length) {
    super(arrayMemoriaClass);
    fComponentTypeInfo = componentTypeInfo;
    fLength = length;
    
    // make space in the list
    for(int i = 0; i < length; ++i) fData.add(null);
    
  }

  @Override
  public void add(Object obj) {
    fData.add(obj);
    fLength += 1;
  }

  @Override
  public Object get(int index) {
    return fData.get(index);
  }

  @Override
  public ArrayTypeInfo getTypeInfo() {
    return fComponentTypeInfo;
  }

  @Override
  public Object getResult() {
    return this;
  }

  @Override
  public int length() {
    return fLength;
  }

  @Override
  public void set(int index, Object value) {
    fData.set(index, value);
  }

}
