package org.memoriadb.core.facade;

import java.io.File;

import org.memoriadb.core.ObjectContainer;
import org.memoriadb.core.facade.nternal.Memoria;

public class MemoriaFactory {
  
  public static IMemoria open(File file) {
    ObjectContainer container = new ObjectContainer(file);
    return new Memoria(container);
  }

}
