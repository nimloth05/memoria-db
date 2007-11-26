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
  
  /**
   * Creates an instance by a string ctor.
   * 
   * @param <T>
   * @param className
   * @param arg
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> T createInstance(String className, String arg) {
    try {
      Class<?> clazz = getClass(className);
      Constructor<?> ctor = clazz.getDeclaredConstructor(arg.getClass());
      ctor.setAccessible(true);
      return (T) ctor.newInstance(arg);
    }
    catch (NoSuchMethodException e) {
      return null;
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }
  
  @SuppressWarnings("unchecked")
  public static <T> T createInstanceWithDefaultOrStringCtor(String className, String arg) {
    try {
      Class<?> clazz = getClass(className);
      for(Constructor<?> ctor: clazz.getDeclaredConstructors()) {
        Class<?>[] parameterTypes = ctor.getParameterTypes();
        if (parameterTypes.length == 0) {
          return (T) ctor.newInstance();
        }
        
        if (parameterTypes.length == 1 && parameterTypes[0].equals(String.class)) {
          return (T) ctor.newInstance(arg);
        }
      }
      throw new MemoriaException("Could not find a default ctor or a ctro with a single String argument");
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
 
  public static ArrayTypeInfo getComponentTypeInfo(Class<?> componentType) {
    int dimension = 1;
    componentType = componentType.getComponentType();
    while (componentType.isArray()) {
      componentType = componentType.getComponentType();
      ++dimension;
    }
    return new ArrayTypeInfo(Type.getType(componentType), dimension, componentType.getName());
  }
  
  @SuppressWarnings("unchecked")
  public static Class<Enum> getCorrectEnumClass(Class<Enum> enumType) {
    Class result = enumType;
    if (result.getSuperclass().isEnum()) result = result.getSuperclass();
    return result;
  }

  public static Class<?> getEnumClass(Class<?> javaClass) {
    if (javaClass.isEnum()) return javaClass;
    if (javaClass.getSuperclass() != null && javaClass.getSuperclass().isEnum()) return javaClass.getSuperclass();
    return null;
  }

  public static Field getField(Class<?> clazz, String name) {
    Field[] fields = clazz.getDeclaredFields();
    for(Field field: fields) {
      if (field.getName().equals(name)) {
        field.setAccessible(true);
        return field;
      }
    }
    
    if (clazz.getSuperclass() == null) throw new SchemaException("No such field. Class: "+ clazz+ " field: "+name);
    return getField(clazz.getSuperclass(), name);
  }
  
  public static Object getFieldValue(Object object, String string) {
    try {
      Field field = ReflectionUtil.getField(object.getClass(), string);
      return field.get(object);
    }
    catch(IllegalAccessException e) {
      throw new MemoriaException(e);
    }
  }
  
  public static ArrayTypeInfo getTypeInfo(Object array) {
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
  
  public static boolean isEnum(Class<?> javaClass) {
    return getEnumClass(javaClass) != null;
  }

  public static boolean isMemoriaTransient(Field field) {
    return Modifier.isTransient(field.getModifiers());
  }

  public static boolean isNonStaticInnerClass(Class<?> javaClass) {
    if (isEnum(javaClass)) return false;
    return javaClass.getEnclosingClass() != null && !Modifier.isStatic(javaClass.getModifiers());
  }

  public static boolean isStatic(Field field) {
    return Modifier.isStatic(field.getModifiers());
  }

  public static void setValueForField(final Object owner, final String fieldName, final Object value) {
    try {
      Field declaredField = getField(owner.getClass(), fieldName);
      declaredField.set(owner, value);
    }
    catch (Exception e) {
      throw new MemoriaException(e);  
    }
  }

  private ReflectionUtil() {}
  
    
}
