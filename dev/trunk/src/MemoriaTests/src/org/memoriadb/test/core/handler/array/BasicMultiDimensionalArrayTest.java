package org.memoriadb.test.core.handler.array;

import java.util.*;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.test.core.testclasses.inheritance.C;
import org.memoriadb.testutil.AbstractMemoriaTest;

public abstract class BasicMultiDimensionalArrayTest extends AbstractMemoriaTest {

  public void test_int_array() {
    int[][] arr = new int[][]{new int[]{1,2}, new int[]{3,4,5}};
    IObjectId id = saveAll(arr);
    
    reopen();
    
    int[][] l1_arr = get(id);
    Arrays.deepEquals(arr, l1_arr);
  }
  
  public void test_int_array_using_save() {
    int[][] arr = new int[][]{new int[]{1,2}, new int[]{3,4,5}};
    save(arr[0]);
    save(arr[1]);
    IObjectId id = save(arr);
    
    reopen();
    
    int[][] l1_arr = get(id);
    Arrays.deepEquals(arr, l1_arr);
  }

  public void test_Integer_array() {
    Integer[][] arr = new Integer[][]{new Integer[]{1,2}, new Integer[]{3,4,5}};
    IObjectId id = saveAll(arr);
    
    reopen();
    
    Integer[][] l1_arr = get(id);
    assertNotSame(arr, l1_arr);
    assertTrue(Arrays.deepEquals(arr, l1_arr));
  }

  public void test_Integer_array_width_null_value() {
    Integer[][] arr = new Integer[][]{new Integer[]{1,null},null};
    IObjectId id = saveAll(arr);
    
    reopen();
    
    Integer[][] l1_arr = get(id);
    assertNotSame(arr, l1_arr);
    assertTrue(Arrays.deepEquals(arr, l1_arr));
  }
  
  public void test_mixed_Object_array() {
    Object[][] arr = new Object[][]{new Object[]{1,new Object[]{3,4, "5"}}, new Object[]{new ArrayList<Integer>(), "7"}};
    IObjectId id = saveAll(arr);
    
    reopen();
    
    Object[][] l1_arr = get(id);
    assertNotSame(arr, l1_arr);
    assertTrue(Arrays.deepEquals(arr, l1_arr));
  }
  
  public void test_Object_array() {
    SimpleTestObj[][] arr = new SimpleTestObj[][]{new SimpleTestObj[]{new SimpleTestObj("1"),new SimpleTestObj("2")}, new SimpleTestObj[]{new SimpleTestObj("3"),new SimpleTestObj("4")}};
    IObjectId id = saveAll(arr);
    
    reopen();
    
    SimpleTestObj[][] l1_arr = get(id);
    assertNotSame(arr, l1_arr);
    assertTrue(Arrays.deepEquals(arr, l1_arr));
  }

  public void test_Object_array_with_interitance() {
    C[][] arr = new C[][]{new C[]{getC(), getC()}, new C[]{getC()}};

    int classCount = query(IMemoriaClass.class).size();
    IObjectId id = saveAll(arr);

    // plus 3 classes: A, B, C 
    assertEquals(classCount+3, query(IMemoriaClass.class).size());
    
    reopen();
    
    C[][] l1_arr = get(id);

    assertNotSame(arr, l1_arr);
    assertTrue(Arrays.deepEquals(arr, l1_arr));
  }
  
  public void test_Object_array_with_null_value() {
    SimpleTestObj[][] arr = new SimpleTestObj[][]{new SimpleTestObj[]{new SimpleTestObj("1"),null}, null};
    IObjectId id = saveAll(arr);
    
    reopen();
    
    SimpleTestObj[][] l1_arr = get(id);
    assertNotSame(arr, l1_arr);
    assertTrue(Arrays.deepEquals(arr, l1_arr));
  }

  private C getC() {
    return new C(1,2l,"3", true, null, (short)7);
  }
  
}
