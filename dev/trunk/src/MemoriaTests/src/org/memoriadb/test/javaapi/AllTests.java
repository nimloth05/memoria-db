package org.memoriadb.test.javaapi;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.javaapi");
    //$JUnit-BEGIN$
    suite.addTestSuite(FileTest.class);
    suite.addTestSuite(IdentityHashMapTest.class);
    suite.addTestSuite(NestedClassTest.class);
    suite.addTestSuite(ListTest.class);
    suite.addTestSuite(LinkedSetTest.class);
    suite.addTestSuite(EnumTest.class);
    suite.addTestSuite(CrcTest.class);
    suite.addTestSuite(SetTest.class);
    //$JUnit-END$
    return suite;
  }

}
