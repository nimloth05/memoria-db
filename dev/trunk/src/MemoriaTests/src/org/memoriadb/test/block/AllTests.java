package org.memoriadb.test.block;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.block");
    //$JUnit-BEGIN$
    suite.addTestSuite(LastWrittenBlockTest.class);
    suite.addTestSuite(SurvivorTest.class);
    suite.addTestSuite(CurrentBlockScenarioTest.class);
    suite.addTestSuite(SurvivorAgentTest.class);
    suite.addTestSuite(BlockTest.class);
    suite.addTestSuite(BlockManagerTest.class);
    suite.addTestSuite(TransactionWriterTest.class);
    suite.addTestSuite(DeleteMarkerTest.class);
    suite.addTestSuite(TestInactiveObjectDataCount.class);
    suite.addTestSuite(MaintenanceFreeBlockManagerTest.class);
    suite.addTestSuite(LastWrittenBlockAfterCrashTest.class);
    //$JUnit-END$
    return suite;
  }

}
