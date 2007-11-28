package org.memoriadb.test.crud.basic;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.crud.basic");
    //$JUnit-BEGIN$
    suite.addTestSuite(LongCrudTest.class);
    suite.addTestSuite(GUIDCrudTest.class);
    //$JUnit-END$
    return suite;
  }

}
