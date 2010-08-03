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

import junit.framework.TestCase;

import org.memoriadb.block.maintenancefree.*;

public class BlockBucketComparatorTest extends TestCase {
  
  private BlockBucketComparator fComparator;
  private BlockBucket fBlock1;
  private BlockBucket fBlock10;
  private BlockBucket fBlock100;

  public void test_compare() {
    assertTrue(fComparator.compare(fBlock1, fBlock1) == 0);
    assertTrue(fComparator.compare(fBlock10, fBlock10) == 0);
    assertTrue(fComparator.compare(fBlock100, fBlock100) == 0);
    assertTrue(fComparator.compare(fBlock1, fBlock10) < 0);
    assertTrue(fComparator.compare(fBlock1, fBlock100) < 0);
    assertTrue(fComparator.compare(fBlock10, fBlock1) > 0);
    assertTrue(fComparator.compare(fBlock100, fBlock10) > 0);
  }
  
  @Override
  protected void setUp() throws Exception {
    fComparator = new BlockBucketComparator();
    
    fBlock1 = new BlockBucket(1);
    fBlock10 = new BlockBucket(10);
    fBlock100 = new BlockBucket(100);
  }
  
}
