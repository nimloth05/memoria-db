package org.memoriadb;

import java.io.*;

import org.memoriadb.core.Bootstrap;
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

  private static final Version fVersion = new Version(0, 0, 0);
  private static final int fFileLayoutVersion = 0;

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

  public static IObjectStore open(CreateConfig config, File path) throws IOException {
    return open(config, new PhysicalFile(path));
  }
  
  public static IObjectStore open(CreateConfig config, IMemoriaFile file) {
    return new ObjectStore(Bootstrap.openOrCreate(config, file, new ObjectModeStrategy()));
  }

  public static IObjectStore open(CreateConfig config, String path) throws IOException {
    return open(config, new File(path));
  }
  
  public static IDataStore openIndataMode(CreateConfig config, String path) throws IOException {
    return openInDataMode(config, new File(path));
  }

  /**
   * @return An ObjectStore backed with an in-memory file
   */
  public static IDataStore openInDataMode(CreateConfig config) {
    return openInDataMode(config, new InMemoryFile());
  }
  
  public static IDataStore openInDataMode(CreateConfig config, File path) throws IOException {
    return openInDataMode(config, new PhysicalFile(path));
  }

  public static IDataStore openInDataMode(CreateConfig config, IMemoriaFile file) {
    return new DataStore(Bootstrap.openOrCreate(config, file, new DataModeStrategy()));
  }
  
  private Memoria() {}

}
