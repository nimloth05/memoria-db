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

package org.memoriadb.test.core.block;

import junit.framework.TestCase;

import org.memoriadb.block.Block;
import org.memoriadb.core.block.BlockRepository;
import org.memoriadb.id.IObjectId;
import org.memoriadb.id.loong.LongId;

public class BlockRepositoryTest extends TestCase {

  private BlockRepository fBlockRepository;
  private Block fBlock1;
  private Block fBlock2;
  private IObjectId fObjectIdA;
  private IObjectId fObjectIdB;
  private IObjectId fObjectIdC;

  public void test_add() {
    fBlockRepository.add(fObjectIdA, fBlock1);
    fBlockRepository.add(fObjectIdC, fBlock2);
    fBlockRepository.add(fObjectIdB, fBlock1);
    
    assertEquals(fBlock1, fBlockRepository.getBlock(fObjectIdA));
    assertEquals(fBlock1, fBlockRepository.getBlock(fObjectIdB));
    assertEquals(fBlock2, fBlockRepository.getBlock(fObjectIdC));
    
//    CollectionUtil.containsAll(fBlockRepository.getObjectInfos(fBlock1), fObjectIdA, fObjectIdB);
//    CollectionUtil.containsAll(fBlockRepository.getObjectInfos(fBlock2), fObjectIdC);
  }
  
  public void test_empty_repo() {
    assertNull(fBlockRepository.getBlock(fObjectIdA));
//    assertTrue(fBlockRepository.getObjectInfos(fBlock1).isEmpty());
  }
  
  public void test_remove() {
    fBlockRepository.add(fObjectIdA, fBlock1);
    fBlockRepository.add(fObjectIdC, fBlock2);
    fBlockRepository.add(fObjectIdB, fBlock1);

//    fBlockRepository.removeBlock(fBlock2);
    fBlockRepository.remove(fObjectIdC);

    assertEquals(fBlock1, fBlockRepository.getBlock(fObjectIdA));
    assertEquals(fBlock1, fBlockRepository.getBlock(fObjectIdB));
    assertNull(fBlockRepository.getBlock(fObjectIdC));
  }
  
//  public void test_removeBlock() {
//    fBlockRepository.add(fObjectIdA, fBlock1);
//    fBlockRepository.add(fObjectIdC, fBlock2);
//    fBlockRepository.add(fObjectIdB, fBlock1);
//    
//    CollectionUtil.containsAll(fBlockRepository.getObjectInfos(fBlock1), fObjectIdA, fObjectIdB);
//    CollectionUtil.containsAll(fBlockRepository.getObjectInfos(fBlock2), fObjectIdC);
//   
//    fBlockRepository.removeBlock(fBlock1);
//    
//    CollectionUtil.containsAll(fBlockRepository.getObjectInfos(fBlock1));
//    CollectionUtil.containsAll(fBlockRepository.getObjectInfos(fBlock2), fObjectIdC);    
//  }
  
  public void test_update() {
    fBlockRepository.add(fObjectIdA, fBlock1);
    fBlockRepository.add(fObjectIdC, fBlock2);
    fBlockRepository.add(fObjectIdB, fBlock1);
    
//    CollectionUtil.containsAll(fBlockRepository.getObjectInfos(fBlock1), fObjectIdA, fObjectIdB);
//    CollectionUtil.containsAll(fBlockRepository.getObjectInfos(fBlock2), fObjectIdC);

    fBlockRepository.update(fObjectIdA, fBlock2);
    
    assertEquals(fBlock2, fBlockRepository.getBlock(fObjectIdA));
    assertEquals(fBlock1, fBlockRepository.getBlock(fObjectIdB));
    assertEquals(fBlock2, fBlockRepository.getBlock(fObjectIdC));
    
//    CollectionUtil.containsAll(fBlockRepository.getObjectInfos(fBlock1), fObjectIdA, fObjectIdB);
//    CollectionUtil.containsAll(fBlockRepository.getObjectInfos(fBlock2), fObjectIdC, fObjectIdA);
  }
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    fBlockRepository = new BlockRepository();
    fBlock1 = new Block(1);
    fBlock2 = new Block(2);
    fObjectIdA = new LongId(7);
    fObjectIdB = new LongId(8);
    fObjectIdC = new LongId(9);
  }
  
}
