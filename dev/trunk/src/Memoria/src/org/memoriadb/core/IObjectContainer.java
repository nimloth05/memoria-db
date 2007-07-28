package org.memoriadb.core;

import java.util.*;

public interface IObjectContainer {

  public void checkSanity();

  public Collection<Object> getAllObjects();

  public Object getObjectById(long objectId);

  public long getObjectId(Object obj);

  public void writeObject(List<Object> asList);

}
