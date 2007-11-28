package org.memoriadb.loadtests;

import org.memoriadb.TestMode;
import org.memoriadb.test.testclasses.SimpleTestObj;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class BlockTest extends AbstractMemoriaTest {

  private static final int COUNT = 10000;

  public void test_many_trxs() {
    for(int i = 0; i < COUNT; ++i) {
      save(new SimpleTestObj(Integer.toString(i)));
    }
    read(COUNT + " objs many trxs ");
  }

  public void test_one_trx() {
    
    beginUpdate();
    for(int i = 0; i < COUNT; ++i) {
      save(new SimpleTestObj(Integer.toString(i)));
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
