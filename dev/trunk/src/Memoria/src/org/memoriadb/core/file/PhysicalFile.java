package org.memoriadb.core.file;

import java.io.*;
import java.nio.channels.FileLock;

import org.memoriadb.exception.MemoriaException;

public class PhysicalFile extends AbstractMemoriaFile {

  private final RandomAccessFile fRandomAccessFile;
  private FileLock fLock;

  public PhysicalFile(String path) {
    try {
      fRandomAccessFile = new RandomAccessFile(path, "rws");
      fLock = fRandomAccessFile.getChannel().tryLock();
    }
    catch (Exception e) {
      throw new MemoriaException(e);
    }

    if (fLock == null) throw new MemoriaException("File is locked: " + path);
  }

  @Override
  public void doAppend(byte[] data) {
    long oldSize = getSize();
    
    // Zuerst wird das File verlängert, damit im Falle eines Crashes das File keine zu kurze Länge hat. 
    try {
      fRandomAccessFile.setLength(oldSize + data.length);
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
    
    internalWrite(data, oldSize);
  }

  @Override
  public void doClose() {
    try {
      if (fLock != null) fLock.release();
      if (fRandomAccessFile != null) fRandomAccessFile.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }


  @Override
  public InputStream doGetInputStream(long position) {
    try {
      fRandomAccessFile.seek(position);
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }

    InputStream stream = new InputStream() {

      @Override
      public int available() throws IOException {
        return (int) (fRandomAccessFile.length() - fRandomAccessFile.getFilePointer());
      }

      @Override
      public void close() throws IOException {
        streamClosed();
        super.close();
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

    // ein 128tel des maximal verfügbaren Speichers wird al grösse angegeben.
    return new BufferedInputStream(stream, (int) Runtime.getRuntime().freeMemory() / 128);

  }

  @Override
  public long doGetSize() {
    try {
      return (int) fRandomAccessFile.length();
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }

  @Override
  public void doWrite(byte[] data, long offset) {
    internalWrite(data, offset);
  }

  private void internalWrite(byte[] data, long offset) {
    try {
      fRandomAccessFile.seek(offset);
      fRandomAccessFile.write(data, 0, data.length);
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }
}
