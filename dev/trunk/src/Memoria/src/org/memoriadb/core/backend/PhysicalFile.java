package org.memoriadb.core.backend;

import java.io.InputStream;

public class PhysicalFile implements IMemoriaFile {

  public PhysicalFile(String path) {
    int bufferSize = (int)Runtime.getRuntime().freeMemory() / 16;
    
  }
  
  @Override
  public void append(byte[] data) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void close() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public InputStream getInputStream() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void write(int offset, byte[] data) {
    // TODO Auto-generated method stub
    
  }

}
