package org.memoriadb.test.core.array.testclasses;

import java.util.Arrays;

import junit.framework.Assert;

import org.memoriadb.test.core.testclasses.SimpleTestObj;

public class MixedArrayContainer implements IArrayContainer {
  
  private final Object[] fArray = new Object[2];

  @Override
  public void assertSame(SimpleTestObj obj) {
    Assert.assertSame(obj, fArray[0]);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final MixedArrayContainer other = (MixedArrayContainer) obj;
    if (!Arrays.equals(fArray, other.fArray)) return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(fArray);
    return result;
  }

  @Override
  public void set() {
    fArray[0] = new SimpleTestObj("1");
    fArray[1] = "Hallo Welt";
  }

}
