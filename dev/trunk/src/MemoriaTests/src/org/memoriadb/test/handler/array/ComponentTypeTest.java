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

package org.memoriadb.test.handler.array;

import junit.framework.TestCase;
import org.memoriadb.core.meta.Type;
import org.memoriadb.core.util.ArrayTypeInfo;
import org.memoriadb.core.util.ReflectionUtil;

public class ComponentTypeTest extends TestCase {

  public void test_multi_dimensional() {
    ArrayTypeInfo arrayTypeInfo = ReflectionUtil.getTypeInfo(new int[0][][]);  
    assertEquals(Type.typeInteger, arrayTypeInfo.getComponentType());
    assertEquals(3, arrayTypeInfo.getDimension());

    arrayTypeInfo = ReflectionUtil.getTypeInfo(new Integer[0][][]);
    assertEquals(Type.typeClass, arrayTypeInfo.getComponentType());
    assertEquals(3, arrayTypeInfo.getDimension());

    arrayTypeInfo = ReflectionUtil.getTypeInfo(new Object[0][][]);
    assertEquals(Type.typeClass, arrayTypeInfo.getComponentType());
    assertEquals(3, arrayTypeInfo.getDimension());
    assertEquals(Object.class.getName(), arrayTypeInfo.getClassName());
  }

  public void test_one_dimensional() {
    ArrayTypeInfo arrayTypeInfo = ReflectionUtil.getTypeInfo(new int[0]);
    assertEquals(Type.typeInteger, arrayTypeInfo.getComponentType());
    assertEquals(1, arrayTypeInfo.getDimension());

    arrayTypeInfo = ReflectionUtil.getTypeInfo(new Integer[0]);
    assertEquals(Type.typeClass, arrayTypeInfo.getComponentType());
    assertEquals(1, arrayTypeInfo.getDimension()); 

    arrayTypeInfo = ReflectionUtil.getTypeInfo(new Object[0]);
    assertEquals(Type.typeClass, arrayTypeInfo.getComponentType());
    assertEquals(1, arrayTypeInfo.getDimension());
    assertEquals(Object.class.getName(), arrayTypeInfo.getClassName());
  }

}
