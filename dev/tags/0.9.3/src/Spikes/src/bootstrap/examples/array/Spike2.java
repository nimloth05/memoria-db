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

public class Spike2 {

  /**
   * @param args
   */
  public static void main(String[] args) {
    Object array = Array.newInstance(int.class, 2, 1);
    Array.set(array, 0, new int[] {1});
    Array.set(array, 1, new int[] {1});
    int[][] intArray = (int[][])array;
    
    int[][] test = new int[2][];
    test[0] = new int[3];
    test[1] = new int[4];
    
    length(1, test);
    
    length(1, new int[2][3][4][5]);
    
    System.out.println("ENDE");
    
  }
  
  private static void length(int level, Object array) {
    int length = Array.getLength(array);
    if (array.getClass().getComponentType().isArray()) {
      ++level;
      for(int i = 0; i < length; ++i) {
        Object entry = Array.get(array, i);
        length(level, entry);
      }
    } 
    System.out.println("legnth " + length + " level " + level);
  }

}
