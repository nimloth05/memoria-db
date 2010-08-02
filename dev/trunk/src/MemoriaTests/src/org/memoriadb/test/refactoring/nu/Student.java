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

package org.memoriadb.test.refactoring.nu;

public class Student extends Person {

  private String fYear;
  private Rank fRank;
  private String fStudentId;
  
  public Rank getRank() {
    return fRank;
  }
  public String getStudentId() {
    return fStudentId;
  }
  public String getYear() {
    return fYear;
  }
  public void setRank(Rank rank) {
    fRank = rank;
  }
  public void setStudentId(String studentId) {
    fStudentId = studentId;
  }
  public void setYear(String year) {
    fYear = year;
  }
  
}
