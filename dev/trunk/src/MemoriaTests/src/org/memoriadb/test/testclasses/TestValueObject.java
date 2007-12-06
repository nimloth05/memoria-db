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
