package org.memoriadb.core.file;

import java.io.IOException;


public interface IFileWriter {

  public void close();

  public IMemoriaFile getFile();

  /**
   * Saves the given object-data to the persistent store.
   */
  public void write(byte[] objectData)  throws IOException;

}
