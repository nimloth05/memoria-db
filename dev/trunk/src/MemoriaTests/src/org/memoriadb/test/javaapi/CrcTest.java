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

package org.memoriadb.test.javaapi;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;

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
