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

package bootstrap.examples.powercut;

import java.io.*;

public class PowerCut {
  
  static final String FILE = "file";
  
  /**
   * Schreibt ein file, in mitten wird die VM gestoppt.
   * @param args
   * @throws IOException 
   */
  public static void main(String[] args) throws IOException {
    RandomAccessFile file = new RandomAccessFile(FILE, "rw");
    System.out.println(file.length() % 10000);
    
    new Thread(new Runnable(){

      @Override
      public void run() {
        Runtime.getRuntime().halt(0);
      }
      
    }).start();
    
    byte[] arr = new byte[10000];
    for(int i = 0; i < 10000; ++i) {
      arr[i] = (byte)i;
    }
    
    for(int i = 0; true; ++i) {
      file.write(arr);
    }
  }
  
}
