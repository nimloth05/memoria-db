package org.memoriadb.test.core.block;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.core.block");
    //$JUnit-BEGIN$
    suite.addTestSuite(BlockManagerTest.class);
    suite.addTestSuite(TransactionWriterTest.class);
    //$JUnit-END$
    return suite;
  }

}
