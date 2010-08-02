package bootstrap.examples.query;

import java.lang.reflect.*;
import java.util.Arrays;

public class Inheritance {
  
  static class A {
    public void f(){
      System.out.println("a");
    }
  }

  static class B extends A {
    @Override
    public void f(){
      System.out.println("b");
    }
  }
  
  public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
    Method method = B.class.getMethod("f");
    method.invoke(new B());
    System.out.println(Arrays.toString(B.class.getMethods()));
  }
  
}
