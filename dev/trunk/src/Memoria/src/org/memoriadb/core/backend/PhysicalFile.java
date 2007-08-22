package org.memoriadb.core.backend;

import java.io.*;
import java.nio.channels.FileLock;

import org.memoriadb.exception.MemoriaException;

public class PhysicalFile implements IMemoriaFile {

  private final RandomAccessFile fRandomAccessFile;
  private FileLock fLock;
  private final String fPath;

  public PhysicalFile(String path) {
    fPath = path;
    try {
      fRandomAccessFile = new RandomAccessFile(path, "rws");
      fLock = fRandomAccessFile.getChannel().tryLock();
    }
    catch(Exception e){
      throw new MemoriaException(e);
    }
    
    if(fLock == null) throw new MemoriaException("File is locked: " + path);
  }
  
  @Override
  public void append(byte[] data) {
    internalWrite(data, getSize());
  }
  
  @Override
  public void close() {
    if(fLock != null) try {
      fLock.release();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
   
    if(fRandomAccessFile != null) try {
      fRandomAccessFile.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public InputStream getInputStream() {
    try {
      fRandomAccessFile.seek(0);
      
      InputStream stream = new InputStream() {

        @Override
        public int available() throws IOException {
          return (int)(fRandomAccessFile.length() - fRandomAccessFile.getFilePointer());
        }

        @Override
        public int read() throws IOException {
          return fRandomAccessFile.read();
        }

        @Override
        public int read(byte[] b) throws IOException {
          return fRandomAccessFile.read(b);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
          return fRandomAccessFile.read(b, off, len);
        }
        
      };
      
      // ein 16tel des maximal verfügbaren Speichers wird al grösse angegeben.
      return new BufferedInputStream(stream, (int)Runtime.getRuntime().freeMemory() / 128);
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }

  public int getSize() {
    try {
      return (int)fRandomAccessFile.length();
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }

  @Override
  public void write(byte[] data, int offset) {
    internalWrite(data, offset);
  }

  private void internalWrite(byte[] data, int offset) {
    try {
      fRandomAccessFile.seek(offset);
      fRandomAccessFile.write(data, 0, data.length);
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }     
  }

}
