package org.memoriadb.test.handler.jdk.awt.color;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.handler.jdk.awt.color");
    //$JUnit-BEGIN$
    suite.addTestSuite(ColorHandlerTest.class);
    //$JUnit-END$
    return suite;
  }

}
