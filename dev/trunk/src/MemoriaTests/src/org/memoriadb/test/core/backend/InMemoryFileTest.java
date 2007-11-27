package org.memoriadb.test.core.backend;

import java.io.*;

import org.memoriadb.core.file.InMemoryFile;
import org.memoriadb.core.util.*;

import junit.framework.TestCase;

public class InMemoryFileTest extends TestCase {
  private InMemoryFile fFile;
  
  public void test() throws IOException {
    assertEquals(0, fFile.getSize());
    
    append((byte)1);
    
    assertEquals(1, fFile.getSize());
    assertEquals(1, fFile.get(0));
    
    append((byte)2);
    assertEquals(2, fFile.getSize());
    assertEquals(1, fFile.get(0));
    assertEquals(2, fFile.get(1));
    
    InputStream stream = fFile.getInputStream();
    assertEquals(2, stream.available());
    assertEquals(1, stream.read());
    assertEquals(1, stream.available());
    assertEquals(2, stream.read());
    assertEquals(0, stream.available());
    assertEquals(-1, stream.read());

  }

  public void test_negative_numbers() {
    append((byte)255);
    append((byte)128);
    
    assertEquals(255, fFile.get(0));
    assertEquals(128, fFile.get(1));
  }
  
  public void test_stream_seek() throws IOException {
    fFile.append(ByteUtil.asByteArray(1));
    fFile.append(ByteUtil.asByteArray(2));
    
    DataInputStream stream = new DataInputStream(fFile.getInputStream(Constants.LONG_LEN));
    assertEquals(2, stream.readLong());
    stream.close();
  }
  
  @Override
  protected void setUp() {
    fFile = new InMemoryFile();
  }
  
  private void append(byte... bytes){
    fFile.append(bytes);
  }
}
