package org.memoriadb.test.util;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("org.memoriadb.util");
    //$JUnit-BEGIN$
    suite.addTestSuite(IdentityHashSetTest.class);
    //$JUnit-END$
    return suite;
  }

}
