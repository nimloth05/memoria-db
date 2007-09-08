package org.memoriadb.test.core.array;

import org.memoriadb.test.core.AbstractObjectStoreTest;
import org.memoriadb.test.core.array.testclasses.*;
import org.memoriadb.test.core.testclasses.SimpleTestObj;

public class ArrayTest extends AbstractObjectStoreTest {
  
    
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
