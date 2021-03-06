/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package bootstrap.examples.array;

import java.lang.reflect.Array;

import junit.framework.TestCase;

public class PrimitiveClassTest extends TestCase {
  public void test() throws ClassNotFoundException {
    
    int[][][] d3= new int[1][1][2];
    
    System.out.println(d3[0][0][1]);
    
    
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
