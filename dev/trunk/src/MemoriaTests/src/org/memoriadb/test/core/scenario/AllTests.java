package org.memoriadb.test.core.scenario;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.core.scenario");
    //$JUnit-BEGIN$
    suite.addTestSuite(BlockReuseTest.class);
    //$JUnit-END$
    return suite;
  }

}
