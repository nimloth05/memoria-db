package org.memoriadb.test.core.handler.list;

import java.util.*;

public class VectorTest extends ListTest{

  @Override
  protected List createList() {
    return new Vector<Object>();
  }

}
