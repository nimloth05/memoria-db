package org.memoriadb;

import org.memoriadb.core.*;
import org.memoriadb.core.file.*;
import org.memoriadb.core.load.ObjectLoader;

/**
 * Facade to a Memoria db
 *  
 * @author msc
 *
 */
public class Memoria {
  
  /**
   * @return An ObjectContainer backed with an in-memory file
   */
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
