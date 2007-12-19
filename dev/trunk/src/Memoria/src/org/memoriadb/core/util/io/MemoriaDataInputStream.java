package org.memoriadb.core.util.io;

import java.io.*;

import org.memoriadb.core.util.ByteUtil;

public class MemoriaDataInputStream extends DataInputStream {

  private static class CountInputStream extends InputStream {

    private final InputStream fInput;
    private long fByteCounter = 0;

    public CountInputStream(InputStream input) {
      fInput = input;
    }
    
    @Override
    public int available() throws IOException {
      return fInput.available();
    }
    
    @Override
    public void close() throws IOException {
      fInput.close();
    }

    public long getReadBytes() {
      return fByteCounter;
    }

    @Override
    public int read() throws IOException {
      ++fByteCounter;
      return fInput.read();
    }

    @Override
    public long skip(long n) throws IOException {
      long skipped = fInput.skip(n);
      fByteCounter += skipped;
      return skipped;
    }
    
    
  }

  private static CountInputStream fCountInputStream;
  
  public MemoriaDataInputStream(InputStream in) {
    super(fCountInputStream = new CountInputStream(in));
  }
  
  @Override
  public int available() throws IOException {
    return fCountInputStream.available();
  }
  
  @Override
  public void close() throws IOException {
    fCountInputStream.close();
  }

  public long getReadBytes() {
    return fCountInputStream.getReadBytes();
  }

  public long readUnsignedLong() throws IOException {
    return ByteUtil.readUnsignedLong(this);
  }

  @Override
  public long skip(long n) throws IOException {
    return fCountInputStream.skip(n);
  }
  
  
  
  
}
