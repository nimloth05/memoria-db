package org.memoriadb.test.testclasses.composite;

public interface IComponent {
  
  public void addChild(IComponent component);
  public int getChildCount();
  public String getData();
  public void setData(String string);

}
