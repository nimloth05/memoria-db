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

  public static void setValueForField(final Object owner, final String fieldName, final Object value) {
    try {
      Field declaredField = getField(owner.getClass(), fieldName);
      declaredField.setAccessible(true);
      
      Object valueToSet = value;
      
      Class<?> type = declaredField.getType();
      if (type.isEnum()) {
        int ordinal = ((Integer)value).intValue();
        valueToSet = type.getEnumConstants()[ordinal];
      }
      
      declaredField.set(owner, valueToSet);
    }
    catch (Exception e) {
      throw new MemoriaException(e);  
    }
  }
  
    
}
