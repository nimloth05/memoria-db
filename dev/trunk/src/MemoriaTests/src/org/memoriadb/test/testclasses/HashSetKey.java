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

public class HashSetKey {
  
  private StringObject fTestObj;

  public HashSetKey() {}
  
  public HashSetKey(String str) {
    fTestObj = new StringObject(str);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final HashSetKey other = (HashSetKey) obj;
    if (fTestObj == null) {
      if (other.fTestObj != null) return false;
    }
    else if (!fTestObj.equals(other.fTestObj)) return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fTestObj == null) ? 0 : fTestObj.hashCode());
    return result;
  }
  
}
