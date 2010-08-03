package org.memoriadb.test.core.block;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite(AllTests.class.getName());
    //$JUnit-BEGIN$
    suite.addTestSuite(BlockRepositoryTest.class);
    //$JUnit-END$
    return suite;
  }

}
