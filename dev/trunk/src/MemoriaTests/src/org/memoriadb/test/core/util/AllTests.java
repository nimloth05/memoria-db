package org.memoriadb.test.core.util;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("org.memoriadb.core.util");
    //$JUnit-BEGIN$
    suite.addTestSuite(IdentityHashSetTest.class);
    //$JUnit-END$
    return suite;
  }

}
