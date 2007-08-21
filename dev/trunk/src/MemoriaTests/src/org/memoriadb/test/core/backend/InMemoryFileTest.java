package org.memoriadb.test.core.backend;

import java.io.*;

import org.memoriadb.core.backend.InMemoryFile;

import junit.framework.TestCase;

public class InMemoryFileTest extends TestCase {
  private InMemoryFile fFile;

  public void test() throws IOException {
    fFile = new InMemoryFile();
    assertEquals(0, fFile.getSize());
    
    append((byte)1);
    
    assertEquals(1, fFile.getSize());
    assertEquals(1, fFile.get(0));
    
    append((byte)2);
    assertEquals(2, fFile.getSize());
    assertEquals(1, fFile.get(0));
    assertEquals(2, fFile.get(1));
    
    InputStream stream = fFile.getInputStream();
    assertEquals(1, stream.read());
    assertEquals(2, stream.read());
    assertEquals(-1, stream.read());

  }
  
  private void append(byte... bytes){
    fFile.append(bytes);
  }
}
