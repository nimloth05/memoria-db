package org.memoriadb.test.refactoring.oldcls;

import java.util.*;

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
