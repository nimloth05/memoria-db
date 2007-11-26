package org.memoriadb.test.core.handler.array;

import junit.framework.TestCase;

import org.memoriadb.core.meta.Type;
import org.memoriadb.util.*;

public class ComponentTypeTest extends TestCase {

  public void test_multi_dimensional() {
    ArrayTypeInfo arrayTypeInfo = ReflectionUtil.getTypeInfo(new int[0][][]);  
    assertEquals(Type.typeInteger, arrayTypeInfo.getComponentType());
    assertEquals(3, arrayTypeInfo.getDimension());

    arrayTypeInfo = ReflectionUtil.getTypeInfo(new Integer[0][][]);
    assertEquals(Type.typeIntegerC, arrayTypeInfo.getComponentType());
    assertEquals(3, arrayTypeInfo.getDimension());

    arrayTypeInfo = ReflectionUtil.getTypeInfo(new Object[0][][]);
    assertEquals(Type.typeClass, arrayTypeInfo.getComponentType());
    assertEquals(3, arrayTypeInfo.getDimension());
    assertEquals(Object.class.getName(), arrayTypeInfo.getClassName());
  }

  public void test_one_dimensional() {
    ArrayTypeInfo arrayTypeInfo = ReflectionUtil.getTypeInfo(new int[0]);
    assertEquals(Type.typeInteger, arrayTypeInfo.getComponentType());
    assertEquals(1, arrayTypeInfo.getDimension());

    arrayTypeInfo = ReflectionUtil.getTypeInfo(new Integer[0]);
    assertEquals(Type.typeIntegerC, arrayTypeInfo.getComponentType());
    assertEquals(1, arrayTypeInfo.getDimension()); 

    arrayTypeInfo = ReflectionUtil.getTypeInfo(new Object[0]);
    assertEquals(Type.typeClass, arrayTypeInfo.getComponentType());
    assertEquals(1, arrayTypeInfo.getDimension());
    assertEquals(Object.class.getName(), arrayTypeInfo.getClassName());
  }

}
