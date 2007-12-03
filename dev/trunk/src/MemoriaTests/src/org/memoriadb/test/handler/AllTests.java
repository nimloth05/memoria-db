package org.memoriadb.test.handler;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.handler");
    
    suite.addTest(org.memoriadb.test.handler.collection.AllTests.suite());
    suite.addTest(org.memoriadb.test.handler.array.AllTests.suite());
    suite.addTest(org.memoriadb.test.handler.map.AllTests.suite());
    suite.addTest(org.memoriadb.test.handler.enu.AllTests.suite());

    return suite;
  }

}
