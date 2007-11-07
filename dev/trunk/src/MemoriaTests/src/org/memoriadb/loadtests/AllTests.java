package org.memoriadb.loadtests;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.loadtests");
    //$JUnit-BEGIN$
    //suite.addTestSuite(LoadTest.class); // FIXME wieder einkommentieren, wenn die Arrays gehen.
    suite.addTestSuite(ObjectRepoTest.class);
    //$JUnit-END$
    return suite;
  }

}
