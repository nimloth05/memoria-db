package bootstrap.core.testclasses;


public class Composite {
  
  private TestObj fObject;
  
  public void set(String text) {
    fObject = new TestObj(text, 0);
  }
  
  public TestObj get() {
    return fObject;
  }
  
}
