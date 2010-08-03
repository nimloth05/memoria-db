/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.test.block;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.block");
    //$JUnit-BEGIN$
    suite.addTestSuite(LastWrittenBlockTest.class);
    suite.addTestSuite(SurvivorTest.class);
    suite.addTestSuite(CurrentBlockScenarioTest.class);
    suite.addTestSuite(SurvivorAgentTest.class);
    suite.addTestSuite(BlockBucketComparatorTest.class);
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
