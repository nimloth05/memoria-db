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

package org.memoriadb.core.refactor;

import org.memoriadb.IDataStore;
import org.memoriadb.IFilter;
import org.memoriadb.IFilterControl;
import org.memoriadb.IRefactor;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.meta.Type;
import org.memoriadb.core.util.ArrayTypeInfo;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.handler.IHandler;
import org.memoriadb.handler.array.DataArray;
import org.memoriadb.handler.array.IArray;
import org.memoriadb.handler.enu.EnumDataObject;
import org.memoriadb.handler.enu.IEnumObject;
import org.memoriadb.handler.field.FieldbasedObject;
import org.memoriadb.handler.field.FieldbasedObjectHandler;
import org.memoriadb.handler.field.IFieldbasedObject;
import org.memoriadb.handler.value.LangValueObject;
import org.memoriadb.id.IObjectId;

import java.util.List;

/**
 * @author Sandro
 */
public class RefactorApi implements IRefactor {

  private final IDataStore fDataStore;

  public RefactorApi(IDataStore dataStore) {
    fDataStore = dataStore;
  }

  @Override
  public IFieldbasedObject asFieldDataObject(Object object) {
    if (object instanceof IFieldbasedObject) return (IFieldbasedObject) object;
    if (object instanceof IDataObject) throw new MemoriaException("object is a DataObject but not of type IFieldbasedObject. " + object);
    
    IObjectId memoriaClassId = fDataStore.getTypeInfo().getMemoriaClassId(object.getClass());
    return new FieldbasedObject(object, memoriaClassId);
  }

  @Override
  public IArray createArray(Class<?> klass, int length) {
    if (!klass.isArray()) throw new MemoriaException("not an array " + klass);
    return new DataArray(getArrayClass(), ReflectionUtil.getComponentTypeInfo(klass), length);
  }

  @Override
  public IArray createArray(String componentType, int dimension, int length) {
    ArrayTypeInfo info = new ArrayTypeInfo(Type.typeClass, dimension, componentType);
    return new DataArray(getArrayClass(), info, length);
  }

  @Override
  public IEnumObject getEnum(String className, final String name) {
    List<IEnumObject> query = fDataStore.query(className, new IFilter<IEnumObject>() {

      @Override
      public boolean accept(IEnumObject object, IFilterControl control) {
        if (object.getName().equals(name)) {
          control.abort();
          return true;
        }
        return false;
      }

    });

    if (!query.isEmpty()) return query.get(0);

    IObjectId memoriaClassId = fDataStore.getTypeInfo().getMemoriaClassId(className);
    if (memoriaClassId == null) throw new MemoriaException("can not create enum, because enum-class has not yet been saved: " + className);
    return new EnumDataObject(memoriaClassId, name);
  }

  @Override
  public <T> LangValueObject<T> getLangValueObject(T value) {
    IObjectId memoriaClassId = fDataStore.getTypeInfo().getMemoriaClassId(value.getClass());
    return new LangValueObject<T>(value, memoriaClassId);
  }

  @Override
  public IMemoriaClass renameClass(String oldName, String newName) {
    for(IMemoriaClass memClass: fDataStore.getTypeInfo().getAllClasses()) {
      if (memClass.getJavaClassName().equals(oldName)) {
        renameClass(memClass, newName);
        return memClass;
      }
    }
    return null;
  }

  private IObjectId getArrayClass() {
    return fDataStore.getDefaultIdProvider().getArrayMemoriaClass();
  }

  private void renameClass(IMemoriaClass memClass, String newName) {
    IHandler handler = memClass.getHandler();
    if (!(handler instanceof FieldbasedObjectHandler)) {
      throw new IllegalArgumentException("Classs '" + memClass.getJavaClassName() + "' does not use FieldbasedObjectHandler but: " + handler.getClass());
    }
    FieldbasedObjectHandler fbHandler = (FieldbasedObjectHandler) handler;
    fbHandler.setClassName(newName);
    
  }

}
