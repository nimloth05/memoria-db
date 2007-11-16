package org.memoriadb.test.core.crud;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.core.crud");
    //$JUnit-BEGIN$
    suite.addTestSuite(UpdateTest.class);
    suite.addTest(org.memoriadb.test.core.crud.basic.AllTests.suite());
    suite.addTestSuite(DeleteTest.class);
    //$JUnit-END$
    return suite;
  }

}
