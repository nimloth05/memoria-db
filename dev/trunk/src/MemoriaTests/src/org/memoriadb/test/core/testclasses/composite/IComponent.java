package org.memoriadb.test.core.testclasses.composite;

public interface IComponent {
  
  public void addChild(IComponent component);
  public int getChildCount();
  public String getData();
  public void setData(String string);

}
