package org.memoriadb.test.core.handler.array;

import junit.framework.TestCase;

import org.memoriadb.core.meta.Type;
import org.memoriadb.util.*;

public class ComponentTypeTest extends TestCase {

  public void test_multi_dimensional() {
    TypeInfo typeInfo = ReflectionUtil.getTypeInfo(new int[0][][]);  
    assertEquals(Type.typeInteger, typeInfo.getComponentType());
    assertEquals(3, typeInfo.getDimension());

    typeInfo = ReflectionUtil.getTypeInfo(new Integer[0][][]);
    assertEquals(Type.typeIntegerC, typeInfo.getComponentType());
    assertEquals(3, typeInfo.getDimension());

    typeInfo = ReflectionUtil.getTypeInfo(new Object[0][][]);
    assertEquals(Type.typeClass, typeInfo.getComponentType());
    assertEquals(3, typeInfo.getDimension());
    assertEquals(Object.class.getName(), typeInfo.getClassName());
  }

  public void test_one_dimensional() {
    TypeInfo typeInfo = ReflectionUtil.getTypeInfo(new int[0]);
    assertEquals(Type.typeInteger, typeInfo.getComponentType());
    assertEquals(1, typeInfo.getDimension());

    typeInfo = ReflectionUtil.getTypeInfo(new Integer[0]);
    assertEquals(Type.typeIntegerC, typeInfo.getComponentType());
    assertEquals(1, typeInfo.getDimension()); 

    typeInfo = ReflectionUtil.getTypeInfo(new Object[0]);
    assertEquals(Type.typeClass, typeInfo.getComponentType());
    assertEquals(1, typeInfo.getDimension());
    assertEquals(Object.class.getName(), typeInfo.getClassName());
  }

}
