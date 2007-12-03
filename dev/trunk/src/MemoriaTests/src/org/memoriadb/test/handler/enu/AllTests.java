package org.memoriadb.test.handler.enu;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.handler.enu");
    //$JUnit-BEGIN$
    suite.addTestSuite(GUIDEnumTest.class);
    suite.addTestSuite(LongEnumTest.class);
    //$JUnit-END$
    return suite;
  }

}
