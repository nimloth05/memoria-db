package org.memoriadb.core.file;


public interface IFileWriter {

  public void close();

  public IMemoriaFile getFile();

  /**
   * Saves the given object-data to the persistent store.
   */
  public void write(byte[] objectData);

}
