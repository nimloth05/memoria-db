package org.memoriadb.test.handler.jdk;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.handler.jdk");
    //$JUnit-BEGIN$

    //$JUnit-END$
    suite.addTest(org.memoriadb.test.handler.jdk.awt.color.AllTests.suite());
    suite.addTest(org.memoriadb.test.handler.jdk.url.AllTests.suite());
    return suite;
  }

}
