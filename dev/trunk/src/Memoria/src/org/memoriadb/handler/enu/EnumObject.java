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

package org.memoriadb.handler.enu;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.id.IObjectId;

/**
 * @author Sandro
 */
public class EnumObject implements IEnumObject {
  
  private IObjectId fMemoriaClassId;
  private String fName;

  public EnumObject(Enum<? extends Enum<?>> enumObj) {
    fName = enumObj.name();
  }

  public EnumObject(IObjectId memoriaClassId) {
    fMemoriaClassId = memoriaClassId;
  }
  
  public EnumObject(IObjectId memoriaClassId, Enum<? extends Enum<?>> enumObj) {
    this(enumObj);
    fMemoriaClassId = memoriaClassId;
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fMemoriaClassId;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object getObject(IMemoriaClass memoriaClass) {
    Class enumClass = (Class) ReflectionUtil.getClass(memoriaClass.getJavaClassName());
    return Enum.valueOf(enumClass, fName);
  }

  @Override
  public String getName() {
    return fName;
  }

  @Override
  public void setName(String name) {
    fName = name;
  }

}
