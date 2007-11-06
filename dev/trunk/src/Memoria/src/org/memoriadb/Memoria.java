package org.memoriadb;

import java.io.IOException;

import org.memoriadb.core.*;
import org.memoriadb.core.block.BlockManager;
import org.memoriadb.core.file.*;
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
  
  private static Version fVersion = new Version(0,0,0);
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
  public static IObjectStore open() {
    IMemoriaFile file = new InMemoryFile();
    return open(file);
  }
  
  public static IObjectStore open(IMemoriaFile file) {
    BlockManager blockManager = new BlockManager();
    ObjectLoader objectLoader = new ObjectLoader(file, blockManager);
    
    FileHeader header = objectLoader.readHeader();
    
    ObjectRepo repo = ObjectRepoFactory.create(header.loadIdFactory());
    if(file.isEmpty()){
      try {
        FileHeaderHelper.writeHeader(file, "class");
      }
      catch (IOException e) {
        throw new MemoriaException(e);
      }
    }
    else {
      
      objectLoader.read(repo);
    }
    return new ObjectStore(repo, file, blockManager);
  }
  
  public static IObjectStore open(String path) {
    IMemoriaFile file = new PhysicalFile(path);
    return open(file);
  }
  
  private Memoria() {}
  
   
}
