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

    if (!"Linux".equals(System.getProperty("os.name"))) {
      assertFalse(file.delete());
      rf.close();
      assertTrue(file.delete());
    } else {
      assertTrue(file.delete());
      rf.close();
      assertFalse(file.delete());
    }

    assertFalse(file.exists());
  }
  
}
