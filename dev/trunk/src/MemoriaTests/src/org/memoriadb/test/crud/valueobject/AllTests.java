package org.memoriadb.test.crud.valueobject;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.core.valueobject");
    //$JUnit-BEGIN$
    suite.addTestSuite(LongValueObjectTest.class);
    suite.addTestSuite(ValueObjectTest.class);
    suite.addTestSuite(GUIDValueObjectTest.class);
    //$JUnit-END$
    return suite;
  }

}
