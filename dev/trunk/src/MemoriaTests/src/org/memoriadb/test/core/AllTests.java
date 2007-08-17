package org.memoriadb.test.core;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.core");
    //$JUnit-BEGIN$
    suite.addTestSuite(FieldTypeTest.class);
    suite.addTestSuite(ObjectContainerTest.class);
    suite.addTestSuite(ObjectRepoTest.class);
    //$JUnit-END$
    return suite;
  }

}
