package org.memoriadb.test.core.handler.array;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.core.handler.array");
    //$JUnit-BEGIN$
    suite.addTestSuite(BasicOneDimensionalArrayTest.class);
    suite.addTestSuite(ComponentTypeTest.class);
    suite.addTestSuite(ArrayTest.class);
    //$JUnit-END$
    return suite;
  }

}
