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

import org.memoriadb.testutil.AbstractMemoriaTest;
import org.memoriadb.CreateConfig;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.block.maintenancefree.MaintenanceFreeBlockManager2;

/**
 * @author Sandro
 */
public final class MaintenanceFreeBlockManger2LiveTest extends AbstractMemoriaTest {

  @Override
  protected void configureOpen(final CreateConfig config) {
    config.setBlockManager(new MaintenanceFreeBlockManager2(70));
  }

  @Override
  protected void configureReopen(final CreateConfig config) {
    configureOpen(config);
  }

  public void test_save_and_load_object() {
    StringObject object = new StringObject(null);
    IObjectId id = save(object);

    reopen();

    StringObject l1_b = get(id);
    assertNull(l1_b.getString());
  }
}

