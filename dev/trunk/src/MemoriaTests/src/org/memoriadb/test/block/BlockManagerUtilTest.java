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
import org.memoriadb.block.BlockManagerUtil;

/**
 * @author Sandro
 */
public final class BlockManagerUtilTest extends TestCase {

  public void test_find_next_power_of_two() {
    assertEquals(0, BlockManagerUtil.getNextPowerOfTwo(1));
    assertEquals(2, BlockManagerUtil.getNextPowerOfTwo(2));
    assertEquals(4, BlockManagerUtil.getNextPowerOfTwo(3));
    assertEquals(4, BlockManagerUtil.getNextPowerOfTwo(4));
    assertEquals(8, BlockManagerUtil.getNextPowerOfTwo(5));
  }

  public void test_getLogFromTwoNumerus() {
    assertEquals(1, BlockManagerUtil.getLog2(2));
    assertEquals(2, BlockManagerUtil.getLog2(4));
    assertEquals(3, BlockManagerUtil.getLog2(8));
    assertEquals(4, BlockManagerUtil.getLog2(16));
    assertEquals(5, BlockManagerUtil.getLog2(32));
    assertEquals(6, BlockManagerUtil.getLog2(64));
    assertEquals(7, BlockManagerUtil.getLog2(128));
    assertEquals(8, BlockManagerUtil.getLog2(256));
    assertEquals(9, BlockManagerUtil.getLog2(512));
    assertEquals(10, BlockManagerUtil.getLog2(1024));
    assertEquals(11, BlockManagerUtil.getLog2(2048));
    assertEquals(12, BlockManagerUtil.getLog2(4096));
    assertEquals(13, BlockManagerUtil.getLog2(8192));
  }

  public void test_findNexPowerOfTwo_with_negative_numbers() {
    try {
      BlockManagerUtil.getNextPowerOfTwo(-1);
    } catch(IllegalArgumentException e) {
      return;
    }
    fail("Exception expected: negative numbers are not allowed");
  }

  public void test_findNextAlignedBlockSize() {
    assertEquals(32, BlockManagerUtil.getNextAlignedBlockSize(28));
    assertEquals(64, BlockManagerUtil.getNextAlignedBlockSize(63));
    assertEquals(64, BlockManagerUtil.getNextAlignedBlockSize(64));
    assertEquals(1024, BlockManagerUtil.getNextAlignedBlockSize(568));
    assertEquals(1024, BlockManagerUtil.getNextAlignedBlockSize(1024));
    assertEquals(1536, BlockManagerUtil.getNextAlignedBlockSize(1123));
    assertEquals(2048, BlockManagerUtil.getNextAlignedBlockSize(1800));
  }

  public void test_getIndexForAlignedBlockSize() {
    assertEquals(5, BlockManagerUtil.getIndexForAlignedBlockSize(32));
    assertEquals(6, BlockManagerUtil.getIndexForAlignedBlockSize(64));
    assertEquals(6, BlockManagerUtil.getIndexForAlignedBlockSize(64));
    assertEquals(10, BlockManagerUtil.getIndexForAlignedBlockSize(1024));
    assertEquals(10, BlockManagerUtil.getIndexForAlignedBlockSize(1024));
    assertEquals(11, BlockManagerUtil.getIndexForAlignedBlockSize(1536));
    assertEquals(12, BlockManagerUtil.getIndexForAlignedBlockSize(2048));
    assertEquals(13, BlockManagerUtil.getIndexForAlignedBlockSize(2560));
  }

  
}
