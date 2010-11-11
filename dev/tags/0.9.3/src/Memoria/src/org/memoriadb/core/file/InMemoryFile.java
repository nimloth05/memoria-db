/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.core.file;

import java.io.*;

import org.memoriadb.core.exception.MemoriaException;

/**
 * @author Sandro
 */
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
  public long doGetSize() {
    return fSize;
  }
  
  @Override
  public void doWrite(byte[] data, long offset) {
    if(data.length+offset > getSize()) throw new MemoriaException("invalid offset: " + offset);
    System.arraycopy(data, 0, fData, (int)offset, data.length);
  }
  
  /**
   * @param index
   * @return The byte at the given index-position as int
   * @param index
   */
  public int get(long index) {
    return fData[(int)index] & 0xFF;
  }
  
  public byte getByte(long index) {
    return fData[(int)index];
  }
  
  /**
   * Shrinks the file to the given size.
   * @param newSize
   * @param newSize
   */
  public void shrink(int newSize) {
    if(newSize >= getSize()) throw new MemoriaException("shrink size too big:" + newSize);
    fSize = newSize;
  }
  
  @Override
  public void sync() {
    // always in sync
  }

  @Override
  public String toString() {
    return "in memory file";
  }

  @Override
  protected InputStream doGetInputStream(final long position) {
    
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
  
}
