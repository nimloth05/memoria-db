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
