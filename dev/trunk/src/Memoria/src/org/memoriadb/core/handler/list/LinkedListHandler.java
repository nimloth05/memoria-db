package org.memoriadb.core.handler.list;

import java.util.*;

import org.memoriadb.core.IDefaultInstantiator;
import org.memoriadb.exception.SchemaCorruptException;

public class LinkedListHandler extends AbstractListHandler {

  @Override
  public void checkCanInstantiateObject(String className, IDefaultInstantiator defaultInstantiator) {
    if (!LinkedList.class.getName().equals(className)) throw new SchemaCorruptException("I am a handler for type " + LinkedList.class.getName() +" but I was called for " + className);
  }

  @Override
  protected List<Object> createList() {
    return new LinkedList<Object>();
  }

}
