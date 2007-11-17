package org.memoriadb.test.core.crud.update;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.core.crud.update");
    //$JUnit-BEGIN$
    suite.addTestSuite(GUIDIdUpdateTest.class);
    suite.addTestSuite(LongIdUpdateTest.class);
    //$JUnit-END$
    return suite;
  }

}
