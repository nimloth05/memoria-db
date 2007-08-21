package org.memoriadb.core.backend;

import java.io.InputStream;

/**
 * 
 * After creation, the file holds a write-lock untill the close-Method is called.
 * 
 * @author msc
 *
 */
public interface IMemoriaFile {
  
  public void append(byte[] data);
  
  /**
   * Releases the write-lock
   */
  public void close();
  
  public InputStream getInputStream();
  
  /**
   * The given offset plus the size of the given byte-array must not exceed the file-site.
   */
  public void write(int offset, byte[] data);
}
