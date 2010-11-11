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

package org.memoriadb.core.util.io;

import java.io.*;

import org.memoriadb.core.exception.MemoriaException;

/**
 * @author Sandro
 */
public final class IOUtil {

  public static void close(Closeable closeable) {
    if (closeable == null) return;

    try {
      closeable.close();
    }
    catch (IOException e) {
      throw new MemoriaException(e);
    }
    
  }
  
  /**
   * Closes the given {@link Closeable} ignoring the I/O exception that might occur.
   * Does nothing if <code>null</code> is given as argument.
   */
  public static void closeSilently(Closeable closeable) {
    if (closeable == null) return;
    try {
      closeable.close();
    } catch (IOException e) {
      // ignore
    }
  }
  
  /**
   * Copies the data from the given input stream to the given output stream.
   * Closes both streams in any case.
   * 
   * @param in stream to read from
   * @param out stream to write to
   * @throws IOException if any I/O exception occurs during reading or writing
   */
  public static final void copyInputStreamToOutputStream(InputStream in, OutputStream out) throws IOException {
    byte[] buffer = new byte[1024 * 4];
    try {
      int len;
      while ((len = in.read(buffer)) >= 0) {
        out.write(buffer, 0, len);
      }
    } finally {
      closeSilently(in);
      closeSilently(out);
    }
  }
  
  private IOUtil() {}

}
