package org.memoriadb.test.core.backend;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.core.backend");
    //$JUnit-BEGIN$
    suite.addTestSuite(PhysicalFileTest.class);
    suite.addTestSuite(InMemoryFileTest.class);
    //$JUnit-END$
    return suite;
  }

}
