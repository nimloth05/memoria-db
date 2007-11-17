package org.memoriadb.test.core.crud.delete;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.core.crud.delete");
    //$JUnit-BEGIN$
    suite.addTestSuite(GUIDIdDeleteTest.class);
    suite.addTestSuite(LongIdDeleteTest.class);
    //$JUnit-END$
    return suite;
  }

}
