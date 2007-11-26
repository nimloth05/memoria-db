package org.memoriadb.test.core;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.core");

    suite.addTest(org.memoriadb.test.core.backend.AllTests.suite());
    suite.addTest(org.memoriadb.test.core.crud.AllTests.suite());
    suite.addTest(org.memoriadb.test.core.block.AllTests.suite());
    suite.addTest(org.memoriadb.test.core.scenario.AllTests.suite());
    suite.addTest(org.memoriadb.test.core.handler.AllTests.suite());

    //$JUnit-BEGIN$
    suite.addTestSuite(LongIdFactoryTest.class);
    suite.addTestSuite(ObjectContainerTest.class);
    suite.addTestSuite(InheritanceTest.class);
    suite.addTestSuite(TypeTest.class);
    suite.addTestSuite(TypeInfoTest.class);
    suite.addTestSuite(ModeTest.class);
    suite.addTestSuite(DefaultInstantiatorTest.class);
    suite.addTestSuite(ObjectRepoTest.class);
    //$JUnit-END$
    return suite;
  }

}
