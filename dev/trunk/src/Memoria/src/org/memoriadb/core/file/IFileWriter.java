package org.memoriadb.core.file;


public interface IFileWriter {

  /**
   * Saves the given data to the persistent store.
   */
  public void write(byte[] data);

}
