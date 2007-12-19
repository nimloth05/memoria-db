package org.memoriadb.test.core.util;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("org.memoriadb.core.util");
    //$JUnit-BEGIN$
    suite.addTestSuite(ByteUtilTest.class);
    suite.addTestSuite(UtilTest.class);
    suite.addTestSuite(IdentityHashSetTest.class);
    //$JUnit-END$
    return suite;
  }

}
