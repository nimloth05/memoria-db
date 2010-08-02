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

import org.memoriadb.core.util.ByteUtil;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MemoriaDataInputStream extends DataInputStream {

  private static class CountInputStream extends InputStream {

    private final InputStream fInput;
    private long fByteCounter = 0;

    public CountInputStream(InputStream input) {
      fInput = input;
    }
    
    @Override
    public int available() throws IOException {
      return fInput.available();
    }
    
    @Override
    public void close() throws IOException {
      fInput.close();
    }

    public long getReadBytes() {
      return fByteCounter;
    }

    @Override
    public int read() throws IOException {
      ++fByteCounter;
      return fInput.read();
    }

    @Override
    public long skip(long n) throws IOException {
      long skipped = fInput.skip(n);
      fByteCounter += skipped;
      return skipped;
    }
    
  }

  // FIXME read-Overloads auch weiterdelegieren
  public MemoriaDataInputStream(InputStream inputStream) {
    super(new CountInputStream(inputStream));
  }
  
  @Override
  public int available() throws IOException {
    return ((CountInputStream)in).available();
  }
  
  @Override
  public void close() throws IOException {
    ((CountInputStream)in).close();
  }

  public long getReadBytes() {
    return ((CountInputStream)in).getReadBytes();
  }

  public long readUnsignedLong() throws IOException {
    return ByteUtil.readUnsignedLong(this);
  }

  @Override
  public long skip(long n) throws IOException {
    return ((CountInputStream)in).skip(n);
  }
  
}
