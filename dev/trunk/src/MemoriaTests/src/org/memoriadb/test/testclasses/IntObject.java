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

public class IntObject {
  
  private int fInt = 0;

  public IntObject() {}
  
  public IntObject(int i) {
    fInt = i;
  }

  public int getInt() {
    return fInt;
  }

  public void setInt(int i) {
    fInt = i;
  }

  @Override
  public String toString() {
    return "IntObject{" + "fInt=" + fInt + '}';
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final IntObject intObject = (IntObject) o;

    if (fInt != intObject.fInt) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return fInt;
  }
}
