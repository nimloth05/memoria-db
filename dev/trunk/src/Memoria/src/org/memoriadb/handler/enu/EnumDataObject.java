/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.handler.enu;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.id.IObjectId;

public class EnumDataObject implements IEnumObject {
  
  private String fName;
  private final IObjectId fMemoriaClassId;

  public EnumDataObject(IObjectId memoriaClassId) {
    fMemoriaClassId = memoriaClassId;
  }

  public EnumDataObject(IObjectId memoriaClassId, String name) {
    fMemoriaClassId = memoriaClassId;
    fName = name;
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fMemoriaClassId;
  }

  @Override
  public Object getObject(IMemoriaClass memoriaClass) {
    return this;
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
