package org.memoriadb.core.facade;

import org.memoriadb.core.ObjectContainer;
import org.memoriadb.core.backend.*;
import org.memoriadb.core.facade.nternal.Memoria;

public class MemoriaFactory {
  
  public static IMemoria open() {
    IMemoriaFile file = new InMemoryFile();
    return open(file);
  }
  
  public static IMemoria open(IMemoriaFile file) {
    ObjectContainer container = new ObjectContainer(file);
    return new Memoria(container);
  }
  
  public static IMemoria open(String path) {
    IMemoriaFile file = new PhysicalFile(path);
    return open(file);
  }

}
