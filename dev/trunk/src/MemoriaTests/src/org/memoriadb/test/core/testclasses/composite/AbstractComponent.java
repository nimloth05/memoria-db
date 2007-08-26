package org.memoriadb.test.core.testclasses.composite;

public abstract class AbstractComponent implements IComponent {
  
  private String fData;

  public String getData() {
    return fData;
  }

  public void setData(String data) {
    fData = data;
  }

}
