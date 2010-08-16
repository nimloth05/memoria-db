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

package org.memoriadb.core.query;

import java.util.List;

import org.memoriadb.*;
import org.memoriadb.core.IObjectRepository;
import org.memoriadb.core.util.ReflectionUtil;

/**
 * @author Sandro
 */
public class ObjectModeQueryStrategy {

  public <T> List<T> query(IObjectRepository objectRepository, Class<T> clazz) {
    return query(objectRepository, clazz, new IFilter<T>() {

      @Override
      public boolean accept(T object, IFilterControl control) {
        return true;
      }
      
    });
  }

   @SuppressWarnings("unchecked")
  public <FILTER, T extends FILTER> List<T> query(IObjectRepository objectRepository, Class<T> clazz, IFilter<FILTER> filter) {
    FilterControl<T> control = new FilterControl<T>();

    for (Object object : objectRepository.getAllUserSpaceObjects(clazz)) {
      T currentObject = (T) object;
      if (filter.accept(currentObject, control)) control.add(currentObject);
      
      if(control.isAbort()) break;
    }
    return control.getResult();
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> query(IObjectRepository objectRepository, String clazz) {
    return (List<T>) query(objectRepository, (Class<Object>) ReflectionUtil.getClass(clazz));
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> query(IObjectRepository objectRepository, String clazz, IFilter<Object> filter) {
    return (List<T>) query(objectRepository, ReflectionUtil.getClass(clazz), filter);
  }

}
