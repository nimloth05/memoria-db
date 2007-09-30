package bootstrap.examples.array;

import java.util.*;

import org.memoriadb.core.handler.def.ArrayComponentType;

public class BitTest {
  
  private static Map<Long, Class<?>> typeMap = buildTypeMap();

  /**
   * @param args
   */
  public static void main(String[] args) {
    Class<?> type = List.class;
    long typeInfo = getTypeInfo(type);
    Class<?> resolvedType = getClassFromTypeInfo(typeInfo);
    System.out.println(resolvedType);
    System.out.println(type.equals(resolvedType));
  }
  
  private static Map<Long, Class<?>> buildTypeMap() {
    Map<Long, Class<?>> result = new HashMap<Long, Class<?>>();
    result.put(5L, List.class);
    return result;
  }

  private static Class<?> getClassFromTypeInfo(long typeInfo) {
    if (typeInfo < 0) {
      int realType = (int) Math.abs(typeInfo);
      return ArrayComponentType.values()[realType].getJavaClass();
    }
    return typeMap.get(typeInfo);
  }

  private static long getTypeInfo(Class<?> type) {
    ArrayComponentType internalType = ArrayComponentType.get(type);
    if (internalType == null) return 5;
    
    long result = ((-1 * internalType.ordinal()));
    return  result;
  }

}
