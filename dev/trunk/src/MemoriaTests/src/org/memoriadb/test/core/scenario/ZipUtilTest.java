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

package org.memoriadb.test.core.scenario;

import java.nio.*;
import java.util.Arrays;

import junit.framework.TestCase;

import org.memoriadb.core.util.ZipUtil;

public class ZipUtilTest extends TestCase {

  public void test_big_buffer() {
    byte[] data = new byte[1000*1000];
    ByteBuffer out = ByteBuffer.wrap(data);
    try {
      int i = 1;
      while(true) {
        out.putLong(i);
        i += 7;
      }
    } catch(BufferOverflowException e) {
    }
    
    compressAndDecompress(data);
  }

  public void test_empty_buffer() {
    byte[] data = new byte[0];
    compressAndDecompress(data);
  }
  
  public void test_short_buffer() {
    byte[] data = "Hello World, this is a short text for you.".getBytes();
    compressAndDecompress(data);
  }
  
  private void compressAndDecompress(byte[] data) {
    byte[] compressed = ZipUtil.compress(data);
    byte[] decompressed = ZipUtil.decompress(compressed);
    assertTrue(Arrays.equals(data, decompressed));
  }
  
}
