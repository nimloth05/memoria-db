package org.memoriadb.test.core.valueobject;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.core.valueobject");
    //$JUnit-BEGIN$
    suite.addTestSuite(ValueObjectTest.class);
    //$JUnit-END$
    return suite;
  }

}
