package org.memoriadb.test.core.handler.list;

import java.util.*;

public class StackTest extends ListTest{

  @Override
  protected List createList() {
    return new Stack<Object>();
  }

}
