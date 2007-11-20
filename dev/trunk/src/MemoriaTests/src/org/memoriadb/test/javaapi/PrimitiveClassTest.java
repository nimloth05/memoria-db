package org.memoriadb.test.javaapi;

import java.lang.reflect.Array;

import junit.framework.TestCase;

public class PrimitiveClassTest extends TestCase {
  public void test() throws ClassNotFoundException {
    
    assertEquals(int.class, (new int[0][2][2]).getClass().getComponentType());
    
    System.out.println(Integer.class);
    System.out.println(int.class.isPrimitive());
    //System.out.println(Class.forName("int"));
    int[][][][] arr = new int[2][][][];
    
    int[][]  marr = (int[][]) Array.newInstance(int.class, 3, 0);
    System.out.println(marr.getClass());
    System.out.println(marr.getClass().getComponentType());
    
    System.out.println(marr[0].getClass());
    System.out.println(marr[0].getClass().getComponentType());
    
    System.out.println("string arr: " + ((String[][][])Array.newInstance(String.class, new int[3])).getClass());
    
    Class<?> type = Array.newInstance(String.class, new int[3]).getClass().getComponentType();
    
    String[][][] strArr = (String[][][]) Array.newInstance(type, 10);
    
    System.out.println(new Integer[0].getClass());
    System.out.println(new byte[0].getClass());
    
    marr = (int[][]) Array.newInstance(marr.getClass().getComponentType(), 3);
//    System.out.println(marr[0].length);
//    
//    int[] dim = (int[]) Array.newInstance(int.class, 3);
//    System.out.println(dim[0]);
    
  }
}
