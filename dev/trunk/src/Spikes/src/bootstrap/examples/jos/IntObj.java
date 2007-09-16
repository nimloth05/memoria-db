package bootstrap.examples.jos;

import java.io.Serializable;

public class IntObj implements Serializable {
  
  private Integer fIntValue = null;
  
  public Object get() {
    return fIntValue;
  }

  public void set(int value) {
    fIntValue = value;
  }

}
