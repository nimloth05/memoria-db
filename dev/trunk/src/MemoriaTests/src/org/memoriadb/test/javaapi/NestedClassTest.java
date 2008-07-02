
package org.memoriadb.test.javaapi;

import java.lang.reflect.*;

import junit.framework.TestCase;

import org.memoriadb.test.testclasses.OuterClass;

public class NestedClassTest extends TestCase {

   public void test_inner_classes() throws SecurityException, NoSuchFieldException {
     Object innerObject = new OuterClass().getInnerClass();
     Class<?> clazz = innerObject.getClass();
     Field field = clazz.getDeclaredField("this$0");
     assertNotNull("this$0 field not found", field);
   }

   public void test_instantiate_inner_class() throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
     Object innerObject = new OuterClass().getInnerClass();
     Class<?> clazz = innerObject.getClass();

     Constructor<?> constructor = clazz.getDeclaredConstructor(OuterClass.class);
     constructor.setAccessible(true);
     assertNotNull(constructor);
     Object newInstance = constructor.newInstance((Object)null);
     assertNotNull(newInstance);
   }

}
