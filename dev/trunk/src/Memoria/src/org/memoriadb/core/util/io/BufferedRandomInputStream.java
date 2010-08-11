/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.memoriadb.core.util.io;

import java.io.*;

/**
 * Provides buffered read from a java.io.RandomAccessFile.
 */
public class BufferedRandomInputStream extends InputStream {

  private RandomAccessFile fInputFile;
  private int fBufferSize; // Current size of the buffer
  private int fBufferPos; // Current read position in the buffer
  /**
   * The absolute position in the file where the buffered region starts.
   */
  private long fBufferStart = 0;

  /**
   * The current value of the RAF's file pointer.
   */
  private long fFilePointer;

  private byte fBuffer[];

  public BufferedRandomInputStream(RandomAccessFile file, int bufferSize) {
    fInputFile = file;
    fBuffer = new byte[bufferSize];
    fFilePointer = 0;
    resetBuffer();
  }

  @Override
  public int available() throws IOException {
    return (int) (fInputFile.length() - (fBufferStart + fBufferPos));
  }

  @Override
  public void close() {
    fInputFile = null;
    fBuffer = null;
  }

  /**
   * Supplies functionality of the {@link java.io.RandomAccessFile#length()}.
   * 
   * @return file length
   * @throws IOException
   */
  public long length() throws IOException {
    return fInputFile.length();
  }

  @Override
  public int read() throws IOException {
    if (fBufferPos >= fBufferSize) {
      if (fillBuffer() <= 0) return -1;
    }
    return fBuffer[fBufferPos++] & 0xFF;
  }

  @Override
  public int read(byte b[], int off, int len) throws IOException {
    int available = fBufferSize - fBufferPos;
    if (available == 0) {
      // attempt to provide at least one byte
      fillBuffer();
      available = fBufferSize - fBufferPos;
    }
    if (available < 0) return -1;
   
    if (len <= available) {
      // the buffer contains all the bytes we need, so copy over and return
      System.arraycopy(fBuffer, fBufferPos, b, off, len);
      fBufferPos += len;
      return len;
    }
    
    // Use portion remaining in the buffer
    System.arraycopy(fBuffer, fBufferPos, b, off, available);
    return available;
  }

  /**
   * Supplies functionality of the {@link java.io.RandomAccessFile#seek(long)} in a buffer-friendly manner.
   * 
   * @param pos
   *          offset
   * @throws IOException
   */
  public void seek(long pos) throws IOException {
    if (pos >= fBufferStart && pos < fBufferStart + fBufferSize) {
      // seeking within the current buffer
      fBufferPos = (int) (pos - fBufferStart);
    }
    else {
      // seeking outside the buffer - just discard the buffer
      fInputFile.seek(pos);
      fFilePointer = pos;
      resetBuffer();
    }
  }

  @Override
  public long skip(long n) throws IOException {
    if (n <= 0) return 0;

    int available = fBufferSize - fBufferPos;
    if (n <= available) {
      fBufferPos += n;
      return n;
    }
    resetBuffer();
    final int skipped = fInputFile.skipBytes((int) (n - available));
    fFilePointer += skipped;
    return available + skipped;
  }

  @Override
  public String toString() {
    return fInputFile.toString();
  }

  private int fillBuffer() throws IOException {
    fBufferPos = 0;
    fBufferStart = fFilePointer;
    fBufferSize = fInputFile.read(fBuffer, 0, fBuffer.length);
    fFilePointer += fBufferSize;
    return fBufferSize;
  }

  private void resetBuffer() {
    fBufferPos = 0;
    fBufferSize = 0;
    fBufferStart = 0;
  }

}
