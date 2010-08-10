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

import org.memoriadb.core.file.read.BlockReader;

/**
 * 
 * Provides buffered read from a java.io.RandomAccessFile.
 * 
 * ATTENION: {@link BlockReader#skip} depends on the skip-implementation of this class.
 */
public class BufferedRandomInputStream extends InputStream {

  private RandomAccessFile inputFile;
  private int buffer_size; // Current size of the buffer
  private int buffer_pos; // Current read position in the buffer
  /**
   * The absolute position in the file where the buffered region starts.
   */
  private long buffer_start = 0;

  /**
   * The current value of the RAF's file pointer.
   */
  private long file_pointer;

  private byte buffer[];

  public BufferedRandomInputStream(RandomAccessFile file, int bufferSize) {
    inputFile = file;
    buffer = new byte[bufferSize];
    file_pointer = 0;
    resetBuffer();
  }

  @Override
  public int available() throws IOException {
    return (int) (inputFile.length() - (buffer_start + buffer_pos));
  }

  @Override
  public void close() {
    inputFile = null;
    buffer = null;
  }

  /**
   * Supplies functionality of the {@link java.io.RandomAccessFile#length()}.
   * 
   * @return file length
   * @throws IOException
   */
  public long length() throws IOException {
    return inputFile.length();
  }

  @Override
  public int read() throws IOException {
    if (buffer_pos >= buffer_size) {
      if (fillBuffer() <= 0) return -1;
    }
    return buffer[buffer_pos++] & 0xFF;
  }

  @Override
  public int read(byte b[], int off, int len) throws IOException {
    int available = buffer_size - buffer_pos;
    if (available < 0) return -1;
   
    // the buffer contains all the bytes we need, so copy over and return
    if (len <= available) {
      System.arraycopy(buffer, buffer_pos, b, off, len);
      buffer_pos += len;
      return len;
    }
    // Use portion remaining in the buffer
    System.arraycopy(buffer, buffer_pos, b, off, available);
    if (fillBuffer() <= 0) return available;
    // recursive call to read again until we have the bytes we need
    return available + read(b, off + available, len - available);
  }

  /**
   * Supplies functionality of the {@link java.io.RandomAccessFile#seek(long)} in a buffer-friendly manner.
   * 
   * @param pos
   *          offset
   * @throws IOException
   */
  public void seek(long pos) throws IOException {
    if (pos >= buffer_start && pos < buffer_start + buffer_size) {
      // seeking within the current buffer
      buffer_pos = (int) (pos - buffer_start);
    }
    else {
      // seeking outside the buffer - just discard the buffer
      inputFile.seek(pos);
      file_pointer = pos;
      resetBuffer();
    }
  }

  @Override
  public long skip(long n) throws IOException {
    if (n <= 0) return 0;

    int available = buffer_size - buffer_pos;
    if (n <= available) {
      buffer_pos += n;
      return n;
    }
    resetBuffer();
    final int skipped = inputFile.skipBytes((int) (n - available));
    file_pointer += skipped;
    return available + skipped;
  }

  @Override
  public String toString() {
    return inputFile.toString();
  }

  private int fillBuffer() throws IOException {
    buffer_pos = 0;
    buffer_start = file_pointer;
    buffer_size = inputFile.read(buffer, 0, buffer.length);
    file_pointer += buffer_size;
    return buffer_size;
  }

  private void resetBuffer() {
    buffer_pos = 0;
    buffer_size = 0;
    buffer_start = 0;
  }

}
