package org.memoriadb.test.block;

import junit.framework.*;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.block");
    //$JUnit-BEGIN$
    suite.addTestSuite(BlockManagerTest.class);
    suite.addTestSuite(LastWrittenBlockTest.class);
    suite.addTestSuite(CurrentBlockScenarioTest.class);
    suite.addTestSuite(SurvivorTest.class);
    suite.addTestSuite(MaintenanceFreeBlockManagerTest.class);
    suite.addTestSuite(LastWrittenBlockAfterCrashTest.class);
    suite.addTestSuite(TransactionWriterTest.class);
    suite.addTestSuite(SurvivorAgentTest.class);
    suite.addTestSuite(BlockTest.class);
    //$JUnit-END$
    return suite;
  }

}
