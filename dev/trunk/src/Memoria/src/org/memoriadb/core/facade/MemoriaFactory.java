package org.memoriadb.core.facade;

import org.memoriadb.core.*;
import org.memoriadb.core.facade.nternal.ObjectContainer;
import org.memoriadb.core.file.*;
import org.memoriadb.core.load.ObjectLoader;
import org.memoriadb.core.repo.*;

public class MemoriaFactory {
  
  public static IObjectContainer open() {
    IMemoriaFile file = new InMemoryFile();
    return open(file);
  }
  
  public static IObjectContainer open(IMemoriaFile file) {
    ObjectRepo repo = ObjectRepoFactory.create();
    ObjectLoader.readIn(file, repo);
    
    return new ObjectContainer(repo, file);
  }
  
  public static IObjectContainer open(String path) {
    IMemoriaFile file = new PhysicalFile(path);
    return open(file);
  }

}
