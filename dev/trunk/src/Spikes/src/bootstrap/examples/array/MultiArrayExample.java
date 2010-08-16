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
import java.util.ArrayList;

public class MultiArrayExample {
  
  private static final String[][] sArray = new String[][] {{"Hallo", "Sandro"}, {"Hallo", "Josiane"}};
  
  private static final Object[] sObjectArray = new Object[] {"Hallo", new Object[] {"Hallo", new long[] {1l}}, 12, new ArrayList<Object>()};
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    Object matrix = Array.newInstance(int[].class, 2);
    Object row0 = Array.newInstance(int.class, 2);
    Object row1 = Array.newInstance(int.class, 2);

    Array.setInt(row0, 0, 1);
    Array.setInt(row0, 1, 2);
    Array.setInt(row1, 0, 3);
    Array.setInt(row1, 1, 4);

    Array.set(matrix, 0, row0);
    Array.set(matrix, 1, row1);
  }

}
