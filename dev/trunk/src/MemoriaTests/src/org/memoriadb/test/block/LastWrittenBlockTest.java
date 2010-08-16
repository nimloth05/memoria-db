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

package org.memoriadb.test.block;

import org.memoriadb.CreateConfig;
import org.memoriadb.block.maintenancefree.MaintenanceFreeBlockManager;
import org.memoriadb.id.IObjectId;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class LastWrittenBlockTest extends AbstractMemoriaTest {

  public void test_reusing_one_block() {
    Object o1 = new Object();
    IObjectId id1 = save(o1);

    reopen();
    assertTrue(getLastBlockInfo().isAppend());
    assertEquals(getBlockManager().getBlock(1).getPosition(), getLastBlockInfo().getPosition());

    // deleting the object, what results in a free block and a deletionMarker.
    delete(get(id1));

    reopen();
    assertTrue(getLastBlockInfo().isAppend());
    assertEquals(getBlockManager().getBlock(2).getPosition(), getLastBlockInfo().getPosition());

    // reuse the free block
    save(new Object());

    reopen();
    assertFalse(getLastBlockInfo().isAppend());
    assertEquals(getBlockManager().getBlock(1).getPosition(), getLastBlockInfo().getPosition());

  }

  @Override
  protected void configureOpen(CreateConfig config) {
    configure(config);
  }

  @Override
  protected void configureReopen(CreateConfig config) {
    configure(config);
  }

  private void configure(CreateConfig config) {
    config.setBlockManager(new MaintenanceFreeBlockManager(0, 0));
  }
  
}
