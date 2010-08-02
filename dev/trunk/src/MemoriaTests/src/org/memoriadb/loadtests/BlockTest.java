/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.loadtests;

import org.memoriadb.TestMode;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class BlockTest extends AbstractMemoriaTest {

  private static final int COUNT = 10000;

  public void test_many_trxs() {
    for(int i = 0; i < COUNT; ++i) {
      save(new StringObject(Integer.toString(i)));
    }
    read(COUNT + " objs many trxs ");
  }

  public void test_one_trx() {
    
    beginUpdate();
    for(int i = 0; i < COUNT; ++i) {
      save(new StringObject(Integer.toString(i)));
    }
    endUpdate();
    
    read(COUNT + " objs one trx ");
  }
  
  @Override
  protected TestMode getTestMode() {
    return TestMode.memory;
  }

  private void read(String string) {
    long time1 = System.nanoTime();
    reopen();
    long time2 = System.nanoTime();
    System.out.println(string  + (time2 - time1)/1000000);
  }
  
}
