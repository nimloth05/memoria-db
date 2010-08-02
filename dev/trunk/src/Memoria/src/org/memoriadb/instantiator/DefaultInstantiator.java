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

package org.memoriadb.instantiator;

import org.memoriadb.core.util.ReflectionUtil;

public class DefaultInstantiator implements IInstantiator {

  @Override
  public void checkCanInstantiateObject(String className) throws CannotInstantiateException {
    try {
      Class<?> clazz = ReflectionUtil.getClassUnsave(className);
      clazz.getDeclaredConstructor();
    }
    catch(ClassNotFoundException e) {
      throw new CannotInstantiateException("Problem during class loading: " + className, e);
    }
    catch(NoSuchMethodException e) {
      throw new CannotInstantiateException("Default constructor not found: " + className, e); 
    }
    catch(SecurityException e) {
      throw new CannotInstantiateException("Security Problem: " + className, e);
    }
  }

  @Override
  public <T> T newInstance(String className) {
    return ReflectionUtil.<T>createInstance(className);
  }

}
