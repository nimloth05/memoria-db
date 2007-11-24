package org.memoriadb.test.core.query;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.core.query");
    //$JUnit-BEGIN$
    suite.addTestSuite(DataModeQueryTest.class);
    suite.addTestSuite(ClassModeQueryTest.class);
    //$JUnit-END$
    return suite;
  }

}
