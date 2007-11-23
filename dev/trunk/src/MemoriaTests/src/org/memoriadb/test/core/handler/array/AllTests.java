package org.memoriadb.test.core.handler.array;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.core.handler.array");
    //$JUnit-BEGIN$
    suite.addTestSuite(LongBasicOneDimensionalArrayTest.class);
    suite.addTestSuite(DataModeOneDimensionalTest.class);
    suite.addTestSuite(ComponentTypeTest.class);
    suite.addTestSuite(GuidBasicOneiDimensionalArrayTest.class);
    suite.addTestSuite(GuidBasicMultiDimensionalArrayTest.class);
    suite.addTestSuite(ArrayTest.class);
    suite.addTestSuite(LongBasicMultiDimensionalArrayTest.class);
    //$JUnit-END$
    return suite;
  }

}
