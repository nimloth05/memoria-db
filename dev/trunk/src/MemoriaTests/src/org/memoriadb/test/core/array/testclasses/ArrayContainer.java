package org.memoriadb.test.core.array.testclasses;

import java.util.Arrays;

import org.memoriadb.test.core.testclasses.SimpleTestObj;

import junit.framework.Assert;

public class ArrayContainer implements IArrayContainer {
    
    public SimpleTestObj[] fArray = new SimpleTestObj[1];
    
    public ArrayContainer() {}
    
    public void assertSame(SimpleTestObj obj) {
      Assert.assertSame(obj, fArray[0]);
    }
    
    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      final ArrayContainer other = (ArrayContainer) obj;
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

    public void set() {
      fArray[0] = new SimpleTestObj("1");
    }
  }
