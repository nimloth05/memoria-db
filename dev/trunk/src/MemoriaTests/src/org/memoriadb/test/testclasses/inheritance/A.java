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

package org.memoriadb.test.testclasses.inheritance;

public class A {

  private int fInt;
  private Long fLong;
  

  public A() {
    
  }
  
  public A(int i, Long l) {
    fInt = i;
    fLong = l;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final A other = (A) obj;
    if (fInt != other.fInt) return false;
    if (fLong == null) {
      if (other.fLong != null) return false;
    }
    else if (!fLong.equals(other.fLong)) return false;
    return true;
  }

  public int getInt() {
    return fInt;
  }

  public Long getLong() {
    return fLong;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + fInt;
    result = prime * result + ((fLong == null) ? 0 : fLong.hashCode());
    return result;
  }

  public void setInt(int i) {
    fInt = i;
  }

  public void setLong(Long l) {
    fLong = l;
  }
  
  

}
