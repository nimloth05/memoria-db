package org.memoriadb.test;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test");
    //$JUnit-BEGIN$
    suite.addTest(org.memoriadb.test.core.AllTests.suite());
    suite.addTest(org.memoriadb.test.javaapi.AllTests.suite());
    suite.addTest(org.memoriadb.test.handler.AllTests.suite());
    suite.addTest(org.memoriadb.test.crud.AllTests.suite());
    suite.addTest(org.memoriadb.test.block.AllTests.suite());

    //$JUnit-END$
    return suite;
  }

}
