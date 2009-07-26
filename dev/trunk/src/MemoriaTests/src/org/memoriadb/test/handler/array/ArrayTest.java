package org.memoriadb.test.handler.array;

import junit.framework.Assert;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.testutil.AbstractMemoriaTest;

import java.util.Arrays;

public class ArrayTest extends AbstractMemoriaTest {
  
  public static interface IArrayContainer {
    public void assertSame(StringObject obj);
    public void set();
  }
  
  public static class MultiDimensionalArrayContainer implements IArrayContainer {
    
    public StringObject[][] fArray = new StringObject[1][1];
    
    public MultiDimensionalArrayContainer() {}
    
    public void assertSame(StringObject obj) {
      Assert.assertSame(obj, fArray[0][0]);
    }
    
    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      final MultiDimensionalArrayContainer other = (MultiDimensionalArrayContainer) obj;
      if (!Arrays.deepEquals(fArray, other.fArray)) return false;
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
      fArray[0][0] = new StringObject("1");
    }
  }
  
  private static class ArrayContainer implements IArrayContainer {
    
    public StringObject[] fArray = new StringObject[1];
    
    public ArrayContainer() {}
    
    public void assertSame(StringObject obj) {
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
      fArray[0] = new StringObject("1");
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

    IObjectId id = saveAll(container);
    reopen();

    IArrayContainer l1_Container = get(id);
    StringObject loadedObj = query(StringObject.class).get(0);

    l1_Container.assertSame(loadedObj);
    assertEquals(container, l1_Container);
  }

}
