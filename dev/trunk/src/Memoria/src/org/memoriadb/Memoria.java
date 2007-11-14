package org.memoriadb;

import java.io.*;

import org.memoriadb.core.*;
import org.memoriadb.core.file.*;
import org.memoriadb.core.file.FileReader;
import org.memoriadb.core.load.ObjectLoader;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.Version;

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
   * Creates an db backed with an in-memory file
   * 
   * @return An ObjectStore backed with an in-memory file
   */
  public static IObjectStore open(CreateConfig config) {
    return open(config, new InMemoryFile());
  }
  
  public static IObjectStore open(CreateConfig config, IMemoriaFile file) {
    if (file == null) throw new IllegalArgumentException("File was null");
    writeHeader(config, file);
    return open((OpenConfig)config, file);
  }

  public static IObjectStore open(CreateConfig config, String path) {
    if(new File(path).exists()) return open((OpenConfig)config, path);
    return open(config, new PhysicalFile(path));
  }

  public static IObjectStore open(OpenConfig config, IMemoriaFile file) {
    if (file == null) throw new IllegalArgumentException("File was null");
    
    FileReader fileReader = new FileReader(file);
    FileHeader header = readHeader(fileReader);
    
    IDefaultInstantiator defaultInstantiator = header.loadDefaultInstantiator();
    ObjectRepo repo = ObjectRepoFactory.create(header.loadIdFactory());
    long headRevision = ObjectLoader.readIn(fileReader, repo, config.getBlockManager(), defaultInstantiator, config.getDBMode());
    TransactionWriter writer = new TransactionWriter(repo, config.getBlockManager(), file, headRevision, config.getDBMode());
    return new ObjectStore(defaultInstantiator, writer, header);
  }
  
  /**
   * @return An ObjectStore backed with a file.
   * @pre The db-file must exist.
   */
  public static IObjectStore open(OpenConfig config, String path) {
    return open(config, new PhysicalFile(path));
  }
  
  private static FileHeader readHeader(FileReader fileReader) {
    try {
      return fileReader.readHeader();
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }

  private static void writeHeader(CreateConfig config, IMemoriaFile file) {
    if (file.isEmpty()) {
      try {
        FileHeaderHelper.writeHeader(file, config.getIdFactoryClassName(), config.getDefaultInstantiatorClassName());
      }
      catch (IOException e) {
        throw new MemoriaException(e);
      }
    }
  }
  private Memoria() {}

}
