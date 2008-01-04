package org.memoriadb.loadtests;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.loadtests");

    //$JUnit-BEGIN$
    suite.addTestSuite(CompressionTest.class);
    suite.addTestSuite(LoadTest.class);
    suite.addTestSuite(BlockTest.class);
    suite.addTestSuite(ComplexGraphTest.class);
    suite.addTestSuite(ObjectRepoTest.class);
    suite.addTestSuite(TreeLoadTest.class);
    suite.addTestSuite(BigInlineGraphTest.class);
    //$JUnit-END$
    return suite;
  }

}
