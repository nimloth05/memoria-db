package org.memoriadb.test.crud.delete;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.crud.delete");
    //$JUnit-BEGIN$
    suite.addTestSuite(LongIdDeleteTest.class);
    suite.addTestSuite(DeleteReferencedObjectTest.class);
    suite.addTestSuite(GUIDIdDeleteTest.class);
    //$JUnit-END$
    return suite;
  }

}
