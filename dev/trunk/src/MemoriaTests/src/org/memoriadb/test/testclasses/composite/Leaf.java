package org.memoriadb.test.testclasses.composite;

import org.memoriadb.test.testclasses.StringObject;

public class Leaf extends AbstractComponent {
  
  private StringObject fTestObj;

  @Override
  public void addChild(IComponent component) {
    throw new RuntimeException("addChild is not allowed on a leaf typ");
  }

  @Override
  public int getChildCount() {
    return 0;
  }

  public StringObject getTestObj() {
    return fTestObj;
  }

  public void setTestObj(StringObject testObj) {
    fTestObj = testObj;
  }
}
