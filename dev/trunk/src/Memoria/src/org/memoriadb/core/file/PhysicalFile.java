package org.memoriadb.core.file;

import java.io.*;
import java.nio.channels.FileLock;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.util.io.BufferedRandomInputStream;

public class PhysicalFile extends AbstractMemoriaFile {

  private final RandomAccessFile fRandomAccessFile;
  private FileLock fLock;
  private final String fPath;

  public PhysicalFile(String path) {
    fPath = path;
    try {
      // FIXME sync only when a block is written
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

    return new BufferedRandomInputStream(fRandomAccessFile, getBufferSize()) {
      @Override
      public void close()  {
        streamClosed();
        super.close();
      }
    };

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

  @Override
  public String toString() {
    return fPath;
  }

  /**
   * ATTENION: The buffer must be big enough to avoid the recursive read in the 
   *           BufferedRandomInputStream to generate a StackoverflowException.
   */
  private int getBufferSize() {
    return (int) Runtime.getRuntime().freeMemory() / 64;
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
