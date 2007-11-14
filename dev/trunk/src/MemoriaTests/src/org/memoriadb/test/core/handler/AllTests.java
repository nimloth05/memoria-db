package org.memoriadb.test.core.handler;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.core.handler");
    
    suite.addTest(org.memoriadb.test.core.handler.list.AllTests.suite());
    
    //$JUnit-BEGIN$
    suite.addTestSuite(LinkedListTest.class);
    suite.addTestSuite(ArrayListTest.class);
    //$JUnit-END$
    return suite;
  }

}
