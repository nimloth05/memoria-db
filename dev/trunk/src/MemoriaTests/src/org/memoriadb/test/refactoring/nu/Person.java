package org.memoriadb.test.refactoring.nu;

import java.util.*;

public abstract class Person implements Iterable<Address> {

  private String fName;
  private final List<Address> fAddresses;

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

  @Override
  public Iterator<Address> iterator() {
    return fAddresses.iterator();
  }
}
