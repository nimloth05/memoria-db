package org.memoriadb.core.file;

import java.io.*;

import org.memoriadb.core.exception.MemoriaException;

public class InMemoryFile extends AbstractMemoriaFile {
  
  /**
   * This amount the file is enlarged additional to the requestet data.
   */
  private static final int GROW = 2<<10;
  
  private byte[] fData = new byte[0];
  private int fSize = 0;
  
  
  @Override
  public void doAppend(byte[] data) {
    if(fSize+data.length > fData.length) {
      byte[] result = new byte[fData.length + data.length + GROW];
      System.arraycopy(fData, 0, result, 0, (int)getSize());
      fData = result;
    }
    
    System.arraycopy(data, 0, fData, (int)getSize(), data.length);
    fSize += data.length;
  }

  @Override
  public void doClose() {
  }

  @Override
  public InputStream doGetInputStream(final long position) {
    
    return new InputStream() {
      long fCursor = position;
      
      @Override
      public int available(){
        return (int)(getSize() - fCursor);
      }

      @Override
      public void close() throws IOException {
        streamClosed();
        super.close();
      }

      @Override
      public int read()  {
        if(fCursor == fSize) return -1;
        return get(fCursor++);
      }
      
    };
  }
  
  @Override
  public long doGetSize() {
    return fSize;
  }
  
  @Override
  public void doWrite(byte[] data, long offset) {
    if(data.length+offset > getSize()) throw new MemoriaException("invalid offset: " + offset);
    System.arraycopy(data, 0, fData, (int)offset, data.length);
  }

  public int get(long index) {
    return fData[(int)index] & 0xFF;
  }
  
  @Override
  public String toString() {
    return "in memory file";
  }
  
}
