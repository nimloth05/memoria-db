package org.memoriadb.core;

import java.io.File;

public final class Memoria {
  
  public static IObjectContainer open(File file) {
    ObjectContainer container = new ObjectContainer(file);
    container.open();
    return container;
  }
  
  private Memoria() {}

}
