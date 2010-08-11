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

package org.memoriadb.core.file;

import java.io.*;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.util.io.BufferedRandomInputStream;

/**
 * Encapsulates a RandomAccessFile. No explicit locking is done.
 * @author msc
 *
 */
public class PhysicalFile extends AbstractMemoriaFile {

  private final RandomAccessFile fRandomAccessFile;
  private final File fPath;
  
  public PhysicalFile(File path) throws IOException {
    fPath = path.getAbsoluteFile();
    makeDirs();
    fRandomAccessFile = new RandomAccessFile(path, "rw");
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
//      return new BufferedInputStream(new FileInputStream(fRandomAccessFile.getFD()) {
//        
//        @Override
//        public void close() throws IOException {
//          streamClosed();
//          super.close();
//        }
//        
//      }, 1024*100);
      return new BufferedRandomInputStream(fRandomAccessFile, getBufferSize()) {
        @Override
        public void close()  {
          streamClosed();
          super.close();
        }
      };
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
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

  public File getFilePath() {
    return fPath;
  }

  @Override
  public void sync() {
    try {
      fRandomAccessFile.getFD().sync();
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
  }

  @Override
  public String toString() {
    return fPath.toString();
  }
  
  private int getBufferSize() {
    return 100*1024;
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

  private void makeDirs() throws IOException {
    File directory = fPath.getParentFile();
    if (directory.exists()) return;
    if (directory.mkdirs()) return;
    throw new IOException("Could not create DB-Directory: " + directory);
  }
}
