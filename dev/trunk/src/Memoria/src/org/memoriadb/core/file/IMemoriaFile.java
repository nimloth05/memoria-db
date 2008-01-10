package org.memoriadb.core.file;

import java.io.InputStream;

/**
 * 
 * After creation, the file holds a write-lock untill the close-Method is called. 
 * 
 * When getInputStream is called, the interface is locked until the stream is closed.
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
  
  /**
   * Returns a stream starting at position 0.
   * 
   * Attention: The stream must be closed!
   * 
   * @return Stream for reading the whole content of the file.
   */
  public InputStream getInputStream();
  
  /**
   * Returns a stream starting at the given <tt>position</tt>.
   * 
   * Attention: The stream must be closed!
   * 
   * @return Stream for reading the whole content of the file.
   */
  public InputStream getInputStream(long position);
  
  public long getSize();

  public boolean isEmpty();

  
  public void sync();

  /**
   * The given offset plus the size of the given byte-array must not exceed the file-site.
   */
  public void write(byte[] data, long offset);
}
