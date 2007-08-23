package org.memoriadb.core.facade;

import org.memoriadb.core.ObjectContainer;
import org.memoriadb.core.facade.nternal.Memoria;
import org.memoriadb.core.file.*;

public class MemoriaFactory {
  
  public static IMemoria open() {
    IMemoriaFile file = new InMemoryFile();
    return open(file);
  }
  
  public static IMemoria open(IMemoriaFile file) {
    ObjectContainer container = new ObjectContainer(file);
    FileWriter fileWriter = new FileWriter(container.getObjecRepo(), file);
    return new Memoria(container, fileWriter);
  }
  
  public static IMemoria open(String path) {
    IMemoriaFile file = new PhysicalFile(path);
    return open(file);
  }

}
