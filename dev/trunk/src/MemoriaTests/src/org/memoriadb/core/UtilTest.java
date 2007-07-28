package org.memoriadb.core;

import java.io.*;

import org.memoriadb.core.Util;

import junit.framework.TestCase;

public class UtilTest extends TestCase {
  
  
  public void test_convertToInt() throws IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    DataOutput output = new DataOutputStream(buffer);
    
    int expected = 1234;
    output.writeInt(1234);
    assertEquals(expected, Util.convertToInt(buffer.toByteArray()));
  }
  
  

}
