package org.memoriadb.test.core.handler.array;

import java.util.Arrays;

import junit.framework.Assert;

import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public class ArrayTest extends AbstractObjectStoreTest {
  
  public static interface IArrayContainer {
    public void assertSame(SimpleTestObj obj);
    public void set();
  }
  
  public static class MultiDimensionalArrayContainer implements IArrayContainer {
    
    public SimpleTestObj[][] fArray = new SimpleTestObj[1][1];
    
    public MultiDimensionalArrayContainer() {}
    
    public void assertSame(SimpleTestObj obj) {
      Assert.assertSame(obj, fArray[0][0]);
    }
    
    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      final MultiDimensionalArrayContainer other = (MultiDimensionalArrayContainer) obj;
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
      fArray[0][0] = new SimpleTestObj("1");
    }
  }
  
  private static class ArrayContainer implements IArrayContainer {
    
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
  
  public void test_multi_dimensional_array_object() {
    internal_test_array_object_container(new MultiDimensionalArrayContainer());
  }
  
  public void test_serialize_array_object() {
    internal_test_array_object_container(new ArrayContainer());
  }
  
  private void internal_test_array_object_container(IArrayContainer container) {
    container.set();

    saveAll(container);
    reopen();

    IArrayContainer loadedContainer = getAll(IArrayContainer.class).get(0);
    SimpleTestObj loadedObj = getAll(SimpleTestObj.class).get(0);

    loadedContainer.assertSame(loadedObj);
    assertEquals(container, loadedContainer);
  }

}
