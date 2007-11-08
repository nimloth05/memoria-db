package org.memoriadb.test.javaapi;

import java.io.*;

import junit.framework.TestCase;

public class FileTest extends TestCase {
  
  private static final String NAME = "test.file";
  
  public void test_RandomAccessFile() throws IOException {
    File file = new File(NAME);
    file.delete();
    
    assertFalse(file.exists());
    RandomAccessFile rf = new RandomAccessFile(NAME, "rws");
    assertTrue(file.exists());

    assertFalse(file.delete());
    rf.close();
    assertTrue(file.delete());

    assertFalse(file.exists());
  }
  
}
