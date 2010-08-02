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

package org.memoriadb.test.testclasses.inheritance;

import org.memoriadb.test.testclasses.StringObject;

public class B extends A {

  private String fString;
  private boolean fBoolean;
  private StringObject fTestObj;

  public B() {}
  
  public B(int i, Long l, String string, boolean b, StringObject testObj) {
    super(i, l);
    setString(string);
    setBoolean(b);
    setTestObj(testObj);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!super.equals(obj)) return false;
    if (getClass() != obj.getClass()) return false;
    final B other = (B) obj;
    if (isBoolean() != other.isBoolean()) return false;
    if (getString() == null) {
      if (other.getString() != null) return false;
    }
    else if (!getString().equals(other.getString())) return false;
    if (getTestObj() == null) {
      if (other.getTestObj() != null) return false;
    }
    else if (!getTestObj().equals(other.getTestObj())) return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + (isBoolean() ? 1231 : 1237);
    result = prime * result + ((getString() == null) ? 0 : getString().hashCode());
    result = prime * result + ((getTestObj() == null) ? 0 : getTestObj().hashCode());
    return result;
  }

  public String getString() {
    return fString;
  }

  public void setString(final String string) {
    fString = string;
  }

  public boolean isBoolean() {
    return fBoolean;
  }

  public void setBoolean(final boolean aBoolean) {
    fBoolean = aBoolean;
  }

  public StringObject getTestObj() {
    return fTestObj;
  }

  public void setTestObj(final StringObject testObj) {
    fTestObj = testObj;
  }
}
