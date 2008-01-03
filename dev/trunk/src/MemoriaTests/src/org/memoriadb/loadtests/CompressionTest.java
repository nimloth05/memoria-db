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
