package org.memoriadb.test.core.enu;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.core.enu");
    //$JUnit-BEGIN$
    suite.addTestSuite(GUIDCrudTest.class);
    suite.addTestSuite(LongCrudTest.class);
    //$JUnit-END$
    return suite;
  }

}