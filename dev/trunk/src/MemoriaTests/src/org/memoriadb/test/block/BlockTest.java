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
import org.memoriadb.block.maintenancefree.BlockBucket;

import java.util.TreeSet;

public class BlockTest extends TestCase {
  
  public void test_compare() {
    TreeSet<BlockBucket> set = new TreeSet<BlockBucket>();
    
    BlockBucket block1 = new BlockBucket(1);
    set.add(block1);
    BlockBucket block10 = new BlockBucket(10);
    set.add(block10);
    BlockBucket block100 = new BlockBucket(100);
    set.add(block100);
    
    assertSame(block1, set.ceiling(new BlockBucket(1)));
    assertSame(block10, set.ceiling(new BlockBucket(2)));
    assertSame(block10, set.ceiling(new BlockBucket(10)));
    assertSame(block100, set.ceiling(new BlockBucket(20)));
    assertSame(block100, set.ceiling(new BlockBucket(100)));
    assertNull(set.ceiling(new BlockBucket(101)));
  }
  
}
