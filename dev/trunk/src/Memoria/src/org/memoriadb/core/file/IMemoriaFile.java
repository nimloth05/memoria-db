/*
 * Copyright 2010 memoria db project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package org.memoriadb.core.file;

import java.io.InputStream;

/**
 * 
 * After creation, the file holds a write-lock untill the close-Method is called. 
 * 
 * When getInputStream is called, the interface is locked until the stream is closed.
 * 
 * @author msc
 *
 */
public interface IMemoriaFile {
  
  public void append(byte[] data);
  
  /**
   * Releases the write-lock
   */
  public void close();
  
  /**
   * Returns a stream starting at position 0.
   * 
   * Attention: The stream must be closed!
   * 
   * @return Stream for reading the whole content of the file.
   */
  public InputStream getInputStream();
  
  /**
   * Returns a stream starting at the given <tt>position</tt>.
   * 
   * Attention: The stream must be closed!
   * 
   * @param position
   * @return Stream for reading the whole content of the file.
   */
  public InputStream getInputStream(long position);
  
  public long getSize();

  public boolean isEmpty();

  
  public void sync();

  /**
   * The given offset plus the size of the given byte-array must not exceed the file-site.
   * @param data
   * @param offset
   */
  public void write(byte[] data, long offset);
}
