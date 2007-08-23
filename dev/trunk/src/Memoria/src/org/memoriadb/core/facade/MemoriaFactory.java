package org.memoriadb.core.facade;

import org.memoriadb.core.*;
import org.memoriadb.core.facade.nternal.Memoria;
import org.memoriadb.core.file.*;
import org.memoriadb.core.load.ObjectLoader;
import org.memoriadb.core.repo.*;

public class MemoriaFactory {
  
  public static IMemoria open() {
    IMemoriaFile file = new InMemoryFile();
    return open(file);
  }
  
  public static IMemoria open(IMemoriaFile file) {
    ObjectRepo repo = ObjectRepoFactory.create();
    ObjectLoader.readIn(file, repo);
    
    return new Memoria(repo, file);
  }
  
  public static IMemoria open(String path) {
    IMemoriaFile file = new PhysicalFile(path);
    return open(file);
  }

}
