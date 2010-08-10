package org.memoriadb.test.core.util;

import java.io.*;

import junit.framework.TestCase;

import org.memoriadb.core.util.io.ByteBufferDataInput;

public class ByteBufferDataInputTest extends TestCase {

  public void test_readUTF() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String string = "Die ist ein kleiner Text mit Sönderzeichlis und so %&ç";
    new DataOutputStream(out).writeUTF(string);
    
    byte[] bytes = out.toByteArray();
    ByteBufferDataInput in = new ByteBufferDataInput(bytes);
    String actual = in.readUTF();
    assertEquals(string, actual);
    assertEquals(0, in.available());
  }
  
}
