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

package org.memoriadb.core.meta;

import org.memoriadb.handler.IHandler;
import org.memoriadb.handler.IHandlerConfig;
import org.memoriadb.id.IObjectId;

public final class MemoriaClass implements IMemoriaClassConfig {

  private final IHandler fHandler;
  private IMemoriaClass fSuperClass;
  private final IObjectId fMemoriaClassId;
  private boolean fIsValueObject;

  public MemoriaClass(IHandler handler, IObjectId memoriaClassId, boolean isValueObject) {
    fIsValueObject = isValueObject;
    fHandler = handler;
    fMemoriaClassId = memoriaClassId;
  }

  @Override
  public IHandler getHandler() {
    return fHandler;
  }

  public String getHandlerName() {
    return fHandler.getClass().getName();
  }

  @Override
  public String getJavaClassName() {
    return fHandler.getClassName();
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fMemoriaClassId;
  }

  @Override
  public IMemoriaClass getSuperClass() {
    return fSuperClass;
  }

  @Override
  public void setSuperClass(IMemoriaClass metaClass) {
    fSuperClass = metaClass;
    if (fHandler instanceof IHandlerConfig) {
      ((IHandlerConfig)fHandler).setSuperClass(fSuperClass);
    }
  }

  @Override
  public String toString() {
    return "javaClass: "+ getJavaClassName() + " handler: " + getHandlerName();
  }

  @Override
  public final boolean isTypeFor(String javaClass) {
    if(getJavaClassName().equals(javaClass)) return true;
    IMemoriaClass superClass = getSuperClass();
    if(superClass == null) return false;
    return superClass.isTypeFor(javaClass);
  }

  @Override
  public boolean isValueObject() {
    return fIsValueObject;
  }

  public void setValueObject(boolean value) {
    fIsValueObject = value;
  }
}
