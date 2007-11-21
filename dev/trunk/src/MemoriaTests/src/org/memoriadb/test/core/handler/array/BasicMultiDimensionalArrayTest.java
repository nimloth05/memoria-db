package org.memoriadb.test.core.handler.array;

import org.memoriadb.testutil.AbstractObjectStoreTest;

public class BasicMultiDimensionalArrayTest extends AbstractObjectStoreTest {
  
  public void test_int_array() {
    int[][] arr = new int[][]{new int[]{1,2}, new int[]{3,4,5}};
    
    saveAll(arr);
    
  }
  
}
