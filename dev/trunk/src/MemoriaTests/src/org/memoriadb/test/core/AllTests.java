package org.memoriadb.test.core;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.core");

    suite.addTest(org.memoriadb.test.core.backend.AllTests.suite());
    suite.addTest(org.memoriadb.test.core.crud.AllTests.suite());
    suite.addTest(org.memoriadb.test.core.block.AllTests.suite());
    suite.addTestSuite(org.memoriadb.test.core.collection.ArrayListTest.class);

    //$JUnit-BEGIN$
    suite.addTestSuite(TypeTest.class);
    suite.addTestSuite(ObjectContainerTest.class);
    suite.addTestSuite(InheritanceTest.class);
    suite.addTestSuite(ObjectRepoTest.class);
    suite.addTestSuite(ModeTest.class);
    suite.addTestSuite(CompositeTest.class);
    //FIXME: TBI
    //suite.addTestSuite(ArrayTest.class);
    //$JUnit-END$
    return suite;
  }

}
