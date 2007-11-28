package org.memoriadb.test.javaapi;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.javaapi");
    //$JUnit-BEGIN$
    suite.addTestSuite(FileTest.class);
    suite.addTestSuite(ListTest.class);
    suite.addTestSuite(LinkedSet.class);
    suite.addTestSuite(EnumTest.class);
    suite.addTestSuite(CrcTest.class);
    //$JUnit-END$
    return suite;
  }

}
