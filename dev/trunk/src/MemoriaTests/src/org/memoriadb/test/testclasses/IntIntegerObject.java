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

package org.memoriadb.test.testclasses;

public class IntIntegerObject {

  private int fInt;
  private Integer fInteger;

  public IntIntegerObject() {}

  public IntIntegerObject(int i, Integer integer) {
    super();
    fInt = i;
    fInteger = integer;
  }

  public int getInt() {
    return fInt;
  }

  public Integer getInteger() {
    return fInteger;
  }

  public void setInt(int i) {
    fInt = i;
  }
  
  public void setInteger(Integer integer) {
    fInteger = integer;
  }

}
