package bootstrap.examples.classloader;

public class A {
  static {
    System.out.println("load A");
  }
  
  public static B f() {
    return null;
  }
  
  public static final void main(String[] args) {
    System.out.println("run");
    f();
  }
}
