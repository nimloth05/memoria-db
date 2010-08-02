/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.test.testclasses.composite;

import java.util.ArrayList;
import java.util.List;

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
