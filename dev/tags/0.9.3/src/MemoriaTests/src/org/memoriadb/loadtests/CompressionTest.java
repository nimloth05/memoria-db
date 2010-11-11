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

package org.memoriadb.loadtests;

import org.memoriadb.CreateConfig;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class CompressionTest extends AbstractMemoriaTest {

  private static final int COUNT = 1000;
  private boolean fUseCompression;

  public void test_with_compressiont() {
    
    fUseCompression=true;
    
    reopen();
    
    executeTest("with compression");
  }

  public void test_without_compressiont() {
    
    fUseCompression=false;
    
    reopen();
    
    executeTest("without compression");
  }

  @Override
  protected void configureReopen(CreateConfig config) {
    config.setUseCompression(fUseCompression);
  }

  private void executeTest(String txt) {
    long start = System.nanoTime();
    
    //beginUpdate();
    for(int i = 0; i < COUNT; ++i) {
      save(new Object());
    }
    //endUpdate();
    
    long end = System.nanoTime();
    
    System.out.println("writing " + COUNT + " objects: " + (end - start)/1000000 + "ms " + txt);
  }

}
