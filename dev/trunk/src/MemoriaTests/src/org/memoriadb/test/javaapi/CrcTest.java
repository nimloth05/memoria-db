package org.memoriadb.test.javaapi;

import java.io.*;
import java.util.zip.CRC32;

import junit.framework.TestCase;

public class CrcTest extends TestCase {
  
  public void test() throws IOException {
    
    final int i = 123456;
    CRC32 crc = new CRC32();
    crc.update(i);
    
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream stream = new DataOutputStream(byteArrayOutputStream);
    stream.writeInt(i);
    
    CRC32 streamCrc = new CRC32();
    streamCrc.update(byteArrayOutputStream.toByteArray());
    
    assertTrue(crc.getValue() != streamCrc.getValue());
    
  }
}
