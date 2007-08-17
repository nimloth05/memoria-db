package bootstrap.examples.jos;

import java.io.Serializable;

public class A implements Serializable {
  
	private B b1;
  private B b2;

  public A() {
    b1 = new B();
    //b2 = new B();
    b2 = b1;
  }
  
}
