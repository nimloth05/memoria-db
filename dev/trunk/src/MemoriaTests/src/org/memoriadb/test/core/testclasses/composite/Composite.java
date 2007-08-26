package org.memoriadb.test.core.testclasses.composite;

import java.util.*;

public class Composite extends AbstractComponent {
  
  private final List<IComponent> fComponents = new ArrayList<IComponent>();
  private int fAddCount;
  

  @Override
  public void addChild(IComponent component) {
    ++fAddCount;
    fComponents.add(component);
  }

  public int getAddCount() {
    return fAddCount;
  }

  public IComponent getChild(int index) {
    return fComponents.get(index);
  }
  
  @Override
  public int getChildCount() {
    return fComponents.size();
  }

  @Override
  public String toString() {
    return "data: "+ getData() + " childCount: " + getChildCount();
  }
  
  
  
}
