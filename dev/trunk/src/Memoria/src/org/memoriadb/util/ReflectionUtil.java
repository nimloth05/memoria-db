package org.memoriadb.util;

import java.lang.reflect.*;

import org.memoriadb.core.meta.Type;
import org.memoriadb.exception.*;

public final class ReflectionUtil {
  
  @SuppressWarnings("unchecked")
  public static <T> T createInstance(String className) {
    try {
      Class<?> clazz = getClass(className);
      Constructor<?> ctor = clazz.getDeclaredConstructor();
      ctor.setAccessible(true);
      return (T) ctor.newInstance();
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }
  
  public static Class<?> getClass(String javaClassName) {
    try {
      return Class.forName(javaClassName);
    }
    catch (ClassNotFoundException e) {
      throw new MemoriaException(e);
    }
  }
  
  public static TypeInfo getComponentTypeInfo(Class<?> componentType) {
    int dimension = 1;
    componentType = componentType.getComponentType();
    while (componentType.isArray()) {
      componentType = componentType.getComponentType();
      ++dimension;
    }
    return new TypeInfo(Type.getType(componentType), dimension, componentType.getName());
  }
 
  public static Field getField(Class<?> clazz, String name) {
    Field[] fields = clazz.getDeclaredFields();
    for(Field field: fields) {
      if (field.getName().equals(name)) return field;
    }
    
    if (clazz.getSuperclass() == null) throw new SchemaException("No such field. Class: "+ clazz+ " field: "+name);
    return getField(clazz.getSuperclass(), name);
  }
  
  public static TypeInfo getTypeInfo(Object array) {
    if(!array.getClass().isArray()) throw new MemoriaException("not an array " + array);
    return getComponentTypeInfo(array.getClass());
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
      Class<?> clazz = getClass(className);
      clazz.getDeclaredConstructor();
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }
  
  public static boolean isMemoriaTransient(Field field) {
    return Modifier.isTransient(field.getModifiers());
  }
  
  public static boolean isNonStaticInnerClass(Class<?> javaClass) {
    return javaClass.getEnclosingClass() != null && !Modifier.isStatic(javaClass.getModifiers());
  }
  
  public static boolean isStatic(Field field) {
    return Modifier.isStatic(field.getModifiers());
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

  private ReflectionUtil() {}
  
    
}
