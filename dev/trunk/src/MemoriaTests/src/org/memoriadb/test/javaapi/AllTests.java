package org.memoriadb.test.javaapi;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.javaapi");
    //$JUnit-BEGIN$
    suite.addTestSuite(FileTest.class);
    //$JUnit-END$
    return suite;
  }

}
