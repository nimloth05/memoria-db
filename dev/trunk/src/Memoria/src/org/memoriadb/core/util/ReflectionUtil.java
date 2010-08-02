/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.core.util;

import org.memoriadb.ValueObject;
import org.memoriadb.WeakRef;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.exception.SchemaException;
import org.memoriadb.core.meta.Type;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class ReflectionUtil {

  public static void checkHasDefaultConstructor(String className) throws SecurityException, NoSuchMethodException {
    Class<?> clazz = getClass(className);
    clazz.getDeclaredConstructor();
  }

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
      for (Constructor<?> ctor : clazz.getDeclaredConstructors()) {
        ctor.setAccessible(true);
        Class<?>[] parameterTypes = ctor.getParameterTypes();
        if (parameterTypes.length == 0) { return (T) ctor.newInstance(); }

        if (parameterTypes.length == 1 && parameterTypes[0].equals(String.class)) { return (T) ctor.newInstance(arg); }
      }
      throw new MemoriaException("Could not find a default ctor or a ctro with a single String argument");
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }
  }

  public static Class<?> getClass(String javaClassName) {
    try {
      return getClassUnsave(javaClassName);
    }
    catch (ClassNotFoundException e) {
      throw new MemoriaException(e);
    }
  }
  
  public static Class<?> getClassUnsave(String javaClassName) throws ClassNotFoundException {
    try {
      return Class.forName(javaClassName);
    }
    catch (ClassNotFoundException e) {
      // caller-class-loader was not successful
    }

    // try the context-class-loader assotiated with the current thread
    return Class.forName(javaClassName, true, Thread.currentThread().getContextClassLoader());
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
    for (Field field : fields) {
      if (field.getName().equals(name)) {
        field.setAccessible(true);
        return field;
      }
    }

    if (clazz.getSuperclass() == null) throw new SchemaException("No such field: " + name + " in  class: " + clazz);
    return getField(clazz.getSuperclass(), name);
  }

  public static Object getFieldValue(Object object, String string) throws IllegalArgumentException, IllegalAccessException {
    Field field = ReflectionUtil.getField(object.getClass(), string);
    return field.get(object);
  }

  public static ArrayTypeInfo getTypeInfo(Object array) {
    if (!array.getClass().isArray()) throw new MemoriaException("not an array " + array);
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

  public static boolean hasValueObjectAnnotation(Class<?> clazz) {
    return clazz.getAnnotation(ValueObject.class) != null;
  }

  public static boolean hasWeakRefAnnotation(Field field) {
    return field.getAnnotation(WeakRef.class) != null;
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
