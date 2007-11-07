package org.memoriadb;

import java.io.IOException;

import org.memoriadb.core.*;
import org.memoriadb.core.block.MaintenanceFreeBlockManager;
import org.memoriadb.core.file.*;
import org.memoriadb.core.id.def.LongIdFactory;
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
   * @return An ObjectStore backed with an in-memory file
   */
  public static IObjectStore open(DBMode mode) {
    IMemoriaFile file = new InMemoryFile();
    return open(file, mode);
  }

  public static IObjectStore open(IMemoriaFile file, DBMode mode) {
    if (file == null) throw new IllegalArgumentException("File was null");

    if (file.isEmpty()) {
      try {
        FileHeaderHelper.writeHeader(file, LongIdFactory.class.getName(), MaintenanceFreeBlockManager.class.getName());
      }
      catch (IOException e) {
        throw new MemoriaException(e);
      }
    }

    MaintenanceFreeBlockManager blockManager = new MaintenanceFreeBlockManager();
    FileReader fileReader = new FileReader(file);
    FileHeader header = readHeader(fileReader);
    
    ObjectRepo repo = ObjectRepoFactory.create(header.loadIdFactory());
    long headRevision = ObjectLoader.readIn(fileReader, repo, blockManager, mode);
    return new ObjectStore(repo, file, blockManager, headRevision);
  }

  public static IObjectStore open(String path, DBMode mode) {
    IMemoriaFile file = new PhysicalFile(path);
    return open(file, mode);
  }

  private static FileHeader readHeader(FileReader fileReader) {
    try {
      return fileReader.readHeader();
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }

  private Memoria() {}

}
