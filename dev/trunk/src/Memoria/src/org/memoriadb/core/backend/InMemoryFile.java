package org.memoriadb.core.backend;

import java.io.InputStream;

public class InMemoryFile implements IMemoriaFile {
  
  /**
   * This amount the file is enlarged additional to the requestet data.
   */
  private static final int GROW = 2<<10;
  
  private byte[] fData = new byte[0];
  private int fSize = 0;
  
  /**
   * Used for the input-stream
   */
  private int fCursor;
  
  @Override
  public void append(byte[] data) {
    if(fSize+data.length > fData.length) {
      byte[] result = new byte[fData.length + data.length + GROW];
      System.arraycopy(fData, 0, result, 0, getSize());
      fData = result;
    }
    
    System.arraycopy(data, 0, fData, getSize(), data.length);
    fSize += data.length;
  }

  @Override
  public void close() {
  }

  public int get(int index) {
    byte result = fData[index];;
    return result<0? result + 256 : result;
  }
  
  @Override
  public InputStream getInputStream() {
    fCursor = 0;
    return new InputStream() {

      @Override
      public int available(){
        return getSize() - fCursor;
      }

      @Override
      public int read()  {
        if(fCursor == fSize) return -1;
        return get(fCursor++);
      }
      
    };
  }

  public int getSize() {
    return fSize;
  }

  @Override
  public void write(byte[] data, int offset) {
    System.arraycopy(data, 0, fData, offset, data.length);
  }
}
