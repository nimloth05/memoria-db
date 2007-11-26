package org.memoriadb.test.core.query;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.core.query");
    //$JUnit-BEGIN$
    suite.addTestSuite(BasicQueryTest.class);
    suite.addTestSuite(ClassModeQueryTest.class);
    suite.addTestSuite(DataModeQueryTest.class);
    //$JUnit-END$
    return suite;
  }

}
