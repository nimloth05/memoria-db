package org.memoriadb.test.handler.jdk.url;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.handler.url");
    //$JUnit-BEGIN$
    suite.addTestSuite(URLHandlerTest.class);
    //$JUnit-END$
    return suite;
  }

}
