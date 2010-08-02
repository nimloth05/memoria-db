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

package org.memoriadb.test.handler.array;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.memoriadb.test.handler.array");
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
