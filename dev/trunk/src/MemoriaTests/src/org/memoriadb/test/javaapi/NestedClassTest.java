package org.memoriadb.test.javaapi;

import java.lang.reflect.*;

import junit.framework.TestCase;

import org.memoriadb.test.testclasses.OuterClass;

public class NestedClassTest extends TestCase {

  public void test_can_call_method_on_anonymous_innerclass_declared_in_other_package_when_setAccessible_is_true() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    callMethodOnArg(new OuterClass().getAnonymousInnerclass());
  }
  
  public void test_can_call_method_on_anonymous_innerclass_with_Interface_as_TypeBase() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    
    Object obj = new Runnable() {
      @SuppressWarnings("unused")
      public void g() {
      }

      @Override
      public void run() {
      }
      
    };
    callMethod(obj, "g");
  }
  
  public void test_can_call_public_method_on_private_innerclass_when_setAccessible_is_true() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    Object innerObject = new OuterClass().getPrivateInnerClass();
    callMethod(innerObject, "f");
  }

  public void test_inner_classes() throws SecurityException, NoSuchFieldException {
    Object innerObject = new OuterClass().getPrivateInnerClass();
    Class<?> clazz = innerObject.getClass();
    Field field = clazz.getDeclaredField("this$0");
    assertNotNull("this$0 field not found", field);
  }

  public void test_instantiate_inner_class() throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
    Object innerObject = new OuterClass().getPrivateInnerClass();
    Class<?> clazz = innerObject.getClass();

    Constructor<?> constructor = clazz.getDeclaredConstructor(OuterClass.class);
    constructor.setAccessible(true);
    assertNotNull(constructor);
    Object newInstance = constructor.newInstance((Object) null);
    assertNotNull(newInstance);
  }
  
  private void callMethod(Object innerObject, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Method f = innerObject.getClass().getMethod(methodName, new Class<?>[] {});
    f.setAccessible(true);
    f.invoke(innerObject, new Object[] {});
  }

  private void callMethodOnArg(Object obj) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    callMethod(obj, "g");
  }
}
