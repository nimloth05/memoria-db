/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.loadtests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.loadtests");

    //$JUnit-BEGIN$
    suite.addTestSuite(CompressionTest.class);
    suite.addTestSuite(LoadTest.class);
    suite.addTestSuite(BlockTest.class);
    suite.addTestSuite(ComplexGraphTest.class);
    suite.addTestSuite(ObjectRepoTest.class);
    suite.addTestSuite(TreeLoadTest.class);
    suite.addTestSuite(BigInlineGraphTest.class);
    //$JUnit-END$
    return suite;
  }

}