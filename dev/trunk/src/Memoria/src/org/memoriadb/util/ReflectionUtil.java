package org.memoriadb.util;

import org.memoriadb.exception.MemoriaException;

public class ReflectionUtil {
  
  @SuppressWarnings("unchecked")
  public static <T> T createInstance(String className) {
    try {
      return (T)Class.forName(className).newInstance();
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }
}
