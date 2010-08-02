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

package org.memoriadb.test.refactoring.old;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Person implements Iterable<Address> {

  private String fName;
  private final List<Address> fAddresses;
  private String fStudentId;
  
  public Person() {
    fAddresses = new ArrayList<Address>();
  }

  public Person addAddress(Address adr) {
    if (!fAddresses.contains(adr)) {
      fAddresses.add(adr);
    }
    return this;
  }

  public String getName() {
    return fName;
  }

  public String getStudentId() {
    return fStudentId;
  }

  @Override
  public Iterator<Address> iterator() {
    return fAddresses.iterator();
  }

  public void setStudentId(String studentId) {
    fStudentId = studentId;
  }
}
