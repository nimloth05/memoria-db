package org.memoriadb.test.crud.testclass;

import junit.framework.Assert;

public class SelfReference {
  
  private final SelfReference fRef;

  public SelfReference() {
    fRef = this;
  }
  
  public void assertRef() {
    Assert.assertSame(fRef, this);
  }

}
