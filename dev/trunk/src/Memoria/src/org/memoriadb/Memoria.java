package org.memoriadb;

import org.memoriadb.core.*;
import org.memoriadb.core.file.*;
import org.memoriadb.core.mode.*;
import org.memoriadb.core.util.Version;

/**
 * 
 * Facade to a Memoria db
 * 
 * @author msc
 * 
 */
public final class Memoria {

  private static Version fVersion = new Version(0, 0, 0);
  private static int fFileLayoutVersion = 0;

  /**
   * @return The Version of the file-layout. Stays stable as long as possible.
   */
  public static int getFileLayoutVersion() {
    return fFileLayoutVersion;
  }

  /**
   * @return Memoria-Version.
   */
  public static Version getMemoriaVersion() {
    return fVersion;
  }

  /**
   * @return An ObjectStore backed with an in-memory file
   */
  public static IObjectStore open(CreateConfig config) {
    return open(config, new InMemoryFile());
  }

  public static IObjectStore open(CreateConfig config, IMemoriaFile file) {
    return new ObjectStore(Bootstrap.openOrCreate(config, file, new ObjectModeStrategy()));
  }

  public static IObjectStore open(CreateConfig config, String path) {
    return open(config, new PhysicalFile(path));
  }
  
  /**
   * @return An ObjectStore backed with an in-memory file
   */
  public static IDataStore openDataMode(CreateConfig config) {
    return openDataMode(config, new InMemoryFile());
  }

  public static IDataStore openDataMode(CreateConfig config, IMemoriaFile file) {
    return new DataStore(Bootstrap.openOrCreate(config, file, new DataModeStrategy()));
  }

  public static IDataStore openDataMode(CreateConfig config, String path) {
    return openDataMode(config, new PhysicalFile(path));
  }
  
  private Memoria() {}

}
