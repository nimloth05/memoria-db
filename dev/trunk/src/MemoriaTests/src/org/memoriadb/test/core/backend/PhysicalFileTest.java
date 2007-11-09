package org.memoriadb.test.core.backend;

import java.io.*;

import org.memoriadb.core.file.*;
import org.memoriadb.util.*;

public class PhysicalFileTest extends junit.framework.TestCase {
  
  private static final String PATH = "testfile.txt";
  private IMemoriaFile fPf;

  public void test() throws IOException {
    assertEquals(0, fPf.getSize());
    
    append((byte)1);
    
    fPf.close();
    fPf = new PhysicalFile(PATH);
    
    InputStream stream = fPf.getInputStream();
    assertEquals(1, stream.read());
    stream.close();
    
    append((byte)0,(byte)3);
    
    write(1, (byte)2);
    
    stream = fPf.getInputStream();
    assertEquals(3, stream.available());
    assertEquals(1, stream.read());
    assertEquals(2, stream.available());
    assertEquals(2, stream.read());
    assertEquals(1, stream.available());
    assertEquals(3, stream.read());
    assertEquals(0, stream.available());
    assertEquals(-1, stream.read());
    stream.close();
  }
  
  public void test_stream_seek() throws IOException {
    fPf.append(ByteUtil.asByteArray(1));
    fPf.append(ByteUtil.asByteArray(2));
    
    DataInputStream stream = new DataInputStream(fPf.getInputStream(Constants.LONG_SIZE));
    assertEquals(2, stream.readLong());
    stream.close();
  }
  
  @Override
  protected void setUp() {
    File file = new File(PATH);
    if(file.exists()) if(!file.delete()) throw new RuntimeException("unable to delete file " + file);
    fPf = new PhysicalFile(PATH);
  }
  
  @Override
  protected void tearDown() {
   fPf.close(); 
  }

  private void append(byte... data) {
    fPf.append(data);
  }
  
  private void write(int offset, byte... data){
    fPf.write(data, offset);
  }
}
