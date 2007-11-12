package org.memoriadb.util;

import java.lang.reflect.*;

import org.memoriadb.exception.*;

public class ReflectionUtil {
  
  @SuppressWarnings("unchecked")
  public static <T> T createInstance(String className) {
    try {
      Class<?> clazz = Class.forName(className);
      Constructor<?> ctor = clazz.getDeclaredConstructor();
      ctor.setAccessible(true);
      return (T) ctor.newInstance();
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }
  
  public static Field getField(Class<?> clazz, String name) {
    Field[] fields = clazz.getDeclaredFields();
    for(Field field: fields) {
      if (field.getName().equals(name)) return field;
    }
    
    if (clazz.getSuperclass() == null) throw new IllegalSchemaException("No such field. Class: "+ clazz+ " field: "+name);
    return getField(clazz.getSuperclass(), name);
  }
  
  public static Object getValueFromField(Object owner, String fieldName) {
    try {
      Field declaredField = getField(owner.getClass(), fieldName);
      declaredField.setAccessible(true);
      return declaredField.get(owner);
    }
    catch (Exception e) {
      throw new MemoriaException(e);  
    }
  }
 
  
  public static boolean hasNoArgCtor(String className) {
    try {
      Class<?> clazz = Class.forName(className);
      clazz.getDeclaredConstructor();
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }

  public static void setValueFromField(Object owner, String fieldName, Object value) {
    try {
      Field declaredField = getField(owner.getClass(), fieldName);
      declaredField.setAccessible(true);
      declaredField.set(owner, value);
    }
    catch (Exception e) {
      throw new MemoriaException(e);  
    }
  }
  
    
}
