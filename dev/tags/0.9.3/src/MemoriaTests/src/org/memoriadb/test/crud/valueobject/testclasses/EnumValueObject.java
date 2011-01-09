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

package org.memoriadb.test.crud.valueobject.testclasses;

import org.memoriadb.ValueObject;
import org.memoriadb.test.testclasses.enums.TestEnum;

/**
 * @author sandro
 *
 */
@ValueObject
public class EnumValueObject {
  
  private TestEnum fEnum = TestEnum.a;

  public TestEnum getEnum() {
    return fEnum;
  }

  public void setEnum(TestEnum enum1) {
    fEnum = enum1;
  }
  
}