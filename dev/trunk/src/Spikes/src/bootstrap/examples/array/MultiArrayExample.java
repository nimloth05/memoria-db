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
