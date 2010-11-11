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

package org.memoriadb.test.crud.testclass;

/**
 * Cyclic dependancy with C1
 * 
 * @author msc
 *
 */
public class Cyclic2 {
  
  private Cyclic1 fC1;
  private String fName;

  public Cyclic2() {
  }
  

  public Cyclic2(String name) {
    super();
    fName = name;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final Cyclic2 other = (Cyclic2) obj;
    if (fName == null) {
      if (other.fName != null) return false;
    }
    else if (!fName.equals(other.fName)) return false;
    return true;
  }

  public Cyclic1 getC1() {
    return fC1;
  }
  
  public String getName() {
    return fName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fName == null) ? 0 : fName.hashCode());
    return result;
  }

  public void setC1(Cyclic1 c1) {
    this.fC1 = c1;
  }

  @Override
  public String toString() {
    return fName;
  }
  

}
