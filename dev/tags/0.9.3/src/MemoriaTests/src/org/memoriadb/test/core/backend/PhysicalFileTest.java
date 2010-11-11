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

package org.memoriadb.test.core.backend;

import org.memoriadb.core.file.IMemoriaFile;
import org.memoriadb.core.file.PhysicalFile;
import org.memoriadb.core.util.ByteUtil;
import org.memoriadb.core.util.Constants;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PhysicalFileTest extends junit.framework.TestCase {
  
  private static final File PATH = new File("testfile.txt");
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
  
  public void test_create_file_in_temp_dir() {
    
  }
  
  public void test_getPosition() throws IOException {
    File path = new File(System.getProperty("java.io.tmpdir") + "/tempTestMemoriaDir/test.mia");
    PhysicalFile physicalFile = new PhysicalFile(path);
    assertTrue(physicalFile.getFilePath().exists());
  }
  
  public void test_stream_seek() throws IOException {
    fPf.append(ByteUtil.asByteArray(1l));
    fPf.append(ByteUtil.asByteArray(2l));
    
    DataInputStream stream = new DataInputStream(fPf.getInputStream(Constants.LONG_LEN));
    assertEquals(2, stream.readLong());
    stream.close();
  }
  
  @Override
  protected void setUp() throws IOException {
    if(PATH.exists()) if(!PATH.delete()) throw new RuntimeException("unable to delete file " + PATH);
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
