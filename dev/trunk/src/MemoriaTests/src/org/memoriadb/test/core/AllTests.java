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

package org.memoriadb.test.core;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.core");

    suite.addTest(org.memoriadb.test.core.backend.AllTests.suite());
    suite.addTest(org.memoriadb.test.core.scenario.AllTests.suite());
    suite.addTest(org.memoriadb.test.core.query.AllTests.suite());
    suite.addTest(org.memoriadb.test.core.util.AllTests.suite());

    //$JUnit-BEGIN$
    suite.addTestSuite(LongIdFactoryTest.class);
    suite.addTestSuite(ObjectContainerTest.class);
    suite.addTestSuite(InheritanceTest.class);
    suite.addTestSuite(TypeTest.class);
    suite.addTestSuite(TypeInfoTest.class);
    suite.addTestSuite(ModeTest.class);
    suite.addTestSuite(RecoveryTest.class);
    suite.addTestSuite(DefaultInstantiatorTest.class);
    suite.addTestSuite(ObjectRepoTest.class);
    //$JUnit-END$
    return suite;
  }

}
