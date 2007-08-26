package org.memoriadb.test.core.testclasses.composite;

import org.memoriadb.test.core.testclasses.SimpleTestObj;

public class Leaf extends AbstractComponent {
  
  private SimpleTestObj fTestObj;

  @Override
  public void addChild(IComponent component) {
    throw new RuntimeException("addChild is not allowed on a leaf typ");
  }

  @Override
  public int getChildCount() {
    return 0;
  }

  public SimpleTestObj getTestObj() {
    return fTestObj;
  }

  public void setTestObj(SimpleTestObj testObj) {
    fTestObj = testObj;
  }
}
