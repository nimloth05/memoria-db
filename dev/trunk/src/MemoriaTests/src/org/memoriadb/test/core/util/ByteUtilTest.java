package org.memoriadb.test.core.util;

import java.io.*;

import org.memoriadb.core.util.ByteUtil;

import junit.framework.TestCase;

public class ByteUtilTest extends TestCase {
  
  public void test(long l) throws IOException {
    byte[] data = write(l);
    long result = read(data);
    assertEquals(l, result);
  }
  
  public void test_serialize_unsigned_long() throws IOException {
    test(0);
    test(1);
    test(127);
    test(128);
    test(1000000l);
    test(Long.MAX_VALUE);
  }
  
  
  public void test_serialize_unsigned_long_size() throws IOException {
    byte[] bytes = write(0);
    assertEquals(1, bytes.length);
    assertEquals(0, bytes[0]);
    
    bytes = write(128);
    assertEquals(2, bytes.length);
    assertEquals(-128, bytes[0]);
    assertEquals(1, bytes[1]);
    
    bytes = write(Long.MAX_VALUE);
    assertEquals(9, bytes.length);
    assertEquals(-1, bytes[0]);
    assertEquals(-1, bytes[1]);
    assertEquals(-1, bytes[2]);
    assertEquals(-1, bytes[3]);
    assertEquals(-1, bytes[4]);
    assertEquals(-1, bytes[5]);
    assertEquals(-1, bytes[6]);
    assertEquals(-1, bytes[7]);
    assertEquals(127, bytes[8]);
  }

  private long read(byte[] data) throws IOException {
    ByteArrayInputStream bis = new ByteArrayInputStream(data);
    DataInputStream dis = new DataInputStream(bis);
    return ByteUtil.readUnsignedLong(dis);
  }
  
  private byte[] write(long l) throws IOException {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(stream);
    ByteUtil.writeUnsignedLong(l, dos);
    return stream.toByteArray();
  }
  
}
