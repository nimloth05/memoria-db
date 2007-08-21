package org.memoriadb.test.core;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.core");

    suite.addTest(org.memoriadb.test.core.backend.AllTests.suite());

    //$JUnit-BEGIN$
    suite.addTestSuite(FieldTypeTest.class);
    suite.addTest(AllLoadTests.suite());
    suite.addTestSuite(ObjectContainerTest.class);
    suite.addTestSuite(ObjectRepoTest.class);
    //$JUnit-END$
    return suite;
  }

}
