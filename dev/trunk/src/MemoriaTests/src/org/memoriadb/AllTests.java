package org.memoriadb;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("bootstrap.core");
    //$JUnit-BEGIN$
    suite.addTest(org.memoriadb.util.AllTests.suite());
    suite.addTest(org.memoriadb.core.AllTests.suite());
    //$JUnit-END$
    return suite;
  }

}
