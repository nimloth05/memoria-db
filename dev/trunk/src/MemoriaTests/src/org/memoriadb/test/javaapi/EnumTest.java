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

package org.memoriadb.test.javaapi;

import junit.framework.TestCase;

public class EnumTest extends TestCase {
  
  
  private enum TestEnum {
    a,
    b {
      
    }
  }
  
  
  public void test_class_is_enum() {
    assertTrue(TestEnum.a.getClass().isEnum());
    assertFalse(TestEnum.b.getClass().isEnum());
    
    assertEquals(TestEnum.values().length, TestEnum.a.getClass().getEnumConstants().length);
    assertNull(TestEnum.b.getClass().getEnumConstants());
  }

}
