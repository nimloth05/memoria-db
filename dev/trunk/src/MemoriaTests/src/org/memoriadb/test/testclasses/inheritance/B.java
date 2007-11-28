package org.memoriadb.test.testclasses.inheritance;

import org.memoriadb.test.testclasses.SimpleTestObj;

public class B extends A {

  public String fString;
  public boolean fBoolean;
  public SimpleTestObj fTestObj;

  
  public B() {
    
  }
  
  public B(int i, Long l, String string, boolean b, SimpleTestObj testObj) {
    super(i, l);
    fString = string;
    fBoolean = b;
    fTestObj = testObj;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!super.equals(obj)) return false;
    if (getClass() != obj.getClass()) return false;
    final B other = (B) obj;
    if (fBoolean != other.fBoolean) return false;
    if (fString == null) {
      if (other.fString != null) return false;
    }
    else if (!fString.equals(other.fString)) return false;
    if (fTestObj == null) {
      if (other.fTestObj != null) return false;
    }
    else if (!fTestObj.equals(other.fTestObj)) return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + (fBoolean ? 1231 : 1237);
    result = prime * result + ((fString == null) ? 0 : fString.hashCode());
    result = prime * result + ((fTestObj == null) ? 0 : fTestObj.hashCode());
    return result;
  }

}
