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

import org.memoriadb.ValueObject;

@ValueObject
public class TestValueObject implements Comparable<TestValueObject> {
  
  private String fValue;
  
  public TestValueObject() {}
  
  public TestValueObject(String value) {
    super();
    fValue = value;
  }

  @Override
  public int compareTo(TestValueObject o) {
    return fValue.compareTo(o.fValue);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final TestValueObject other = (TestValueObject) obj;
    if (fValue == null) {
      if (other.fValue != null) return false;
    }
    else if (!fValue.equals(other.fValue)) return false;
    return true;
  }

  public String getValue() {
    return fValue;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fValue == null) ? 0 : fValue.hashCode());
    return result;
  }

  public void setValue(String value) {
    fValue = value;
  }
  
  
  
  

}
