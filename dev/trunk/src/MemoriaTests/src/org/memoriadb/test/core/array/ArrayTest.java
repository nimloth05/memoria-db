package org.memoriadb.test.core.array;

import java.util.Arrays;

import org.memoriadb.test.core.array.testclasses.*;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public class ArrayTest extends AbstractObjectStoreTest {
  
    
  public void test_multi_dimensional_array_object() {
    internal_test_array_object_container(new MultiDimensionalArrayContainer());
  }
  
  public void test_multi_equals() {
    String[] array3 = new String[] {"1"};
    String[] array4 = new String[] {"1"};
    assertTrue( Arrays.equals(array3, array4) );
    
    String[][] array1 = new String[][] {new String[] {"1"}};
    String[][] array2 = new String[][] {new String[] {"1"}};
    
    assertTrue( Arrays.deepEquals(array1, array2) );
  }
  
  public void test_serialize_array_object() {
    internal_test_array_object_container(new ArrayContainer());
  }
  
  public void test_serialize_mixed_array_object() {
    internal_test_array_object_container(new MixedArrayContainer());
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
