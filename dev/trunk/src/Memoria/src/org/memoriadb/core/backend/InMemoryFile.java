package org.memoriadb.core.backend;

import java.io.*;

public class InMemoryFile implements IMemoriaFile {
  
  private byte[] fData = new byte[0];
  
  @Override
  public void append(byte[] data) {
    byte[] result = new byte[fData.length + data.length];
    System.arraycopy(fData, 0, result, 0, getSize());
    System.arraycopy(data, 0, result, getSize(), data.length);
    fData = result;
  }

  @Override
  public void close() {
  }

  public byte get(int index) {
    return fData[index];
  }
  
  @Override
  public InputStream getInputStream() {
    return new ByteArrayInputStream(fData);
  }

  public int getSize() {
    return fData.length;
  }

  @Override
  public void write(int offset, byte[] data) {
    System.arraycopy(data, 0, fData, offset, data.length);
  }
}
