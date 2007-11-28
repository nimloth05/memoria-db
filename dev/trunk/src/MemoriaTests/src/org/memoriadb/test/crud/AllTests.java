package org.memoriadb.test.crud;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for Basic CRUD Operations");
    //$JUnit-BEGIN$
    suite.addTest(org.memoriadb.test.crud.update.AllTests.suite());
    suite.addTest(org.memoriadb.test.crud.basic.AllTests.suite());
    suite.addTest(org.memoriadb.test.crud.delete.AllTests.suite());
    //$JUnit-END$
    return suite;
  }

}
