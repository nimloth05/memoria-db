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
   * @return An ObjectStore backed with an in-memory file
   */
  public static IObjectStore open() {
    IMemoriaFile file = new InMemoryFile();
    return open(file);
  }
  
  public static IObjectStore open(IMemoriaFile file) {
    ObjectRepo repo = ObjectRepoFactory.create();
    ObjectLoader.readIn(file, repo);
    return new ObjectStore(repo, file);
  }
  
  public static IObjectStore open(String path) {
    IMemoriaFile file = new PhysicalFile(path);
    return open(file);
  }

}
