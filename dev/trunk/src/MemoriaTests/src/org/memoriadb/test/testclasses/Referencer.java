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

package org.memoriadb.test.testclasses;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


public class Referencer {
  
  private Object fObject;
  
  public Object get() {
    return fObject;
  }
  
  public String getStringValueFromReferencee() throws Exception {
    Method declaredMethod = fObject.getClass().getDeclaredMethod("getString");
    return declaredMethod.invoke(fObject).toString();
  }
  
  public <T> void set(Class<T> referenceeType, String value) throws Exception {
    Constructor<T> constructor = referenceeType.getDeclaredConstructor(String.class);
    fObject = constructor.newInstance(value);
  }
  
  
}
 