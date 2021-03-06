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

package org.memoriadb.test.crud.update;

import org.memoriadb.core.IObjectInfo;
import org.memoriadb.core.util.Constants;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.IntObject;
import org.memoriadb.testutil.AbstractMemoriaTest;

public abstract class UpdateTest extends AbstractMemoriaTest {

  public void test_basic_revision_handling() {
    Object o1 = new Object();
    IObjectId id1 = save(o1);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 2, getRevision(id1));

    Object o2 = new Object();
    IObjectId id2 = save(o2);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 3, getRevision(id2));

    delete(o1);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 4, getRevision(id1));
    assertEquals(Constants.INITIAL_HEAD_REVISION + 3, getRevision(id2));
    
    reopen();
    
    assertEquals(Constants.INITIAL_HEAD_REVISION + 4, getRevision(id1));
    assertEquals(Constants.INITIAL_HEAD_REVISION + 3, getRevision(id2));
  }

  public void test_revision_for_change_on_original_and_on_l1() {
    IntObject a = new IntObject(0);
    IObjectId a_id = save(a);
    IObjectInfo info = fObjectStore.getObjectInfo(a);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 2, getRevision(info));
    assertEquals(0, info.getOldGenerationCount());

    a.setInt(1);
    save(a);

    assertEquals(Constants.INITIAL_HEAD_REVISION + 3, getRevision(info));
    assertEquals(1, info.getOldGenerationCount());
    a = null;

    reopen();

    IntObject a_l1 = fObjectStore.get(a_id);
    info = fObjectStore.getObjectInfo(a_l1);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 3, getRevision(info));
    assertEquals(1, info.getOldGenerationCount());

    a_l1.setInt(2);
    save(a_l1);
    a_l1 = null;
    assertEquals(Constants.INITIAL_HEAD_REVISION + 4, getRevision(info));
    assertEquals(2, info.getOldGenerationCount());

    reopen();

    IntObject a_l2 = fObjectStore.get(a_id);
    info = fObjectStore.getObjectInfo(a_l2);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 4, getRevision(info));
    assertEquals(2, info.getOldGenerationCount());
  }

  public void test_version_for_many_changes_in_one_transaction_on_original() {
    IntObject a = new IntObject(0);
    IObjectId a_id = save(a);
    IObjectInfo info = fObjectStore.getObjectInfo(a);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 2, getRevision(info));
    assertEquals(0, info.getOldGenerationCount());

    fObjectStore.beginUpdate();

    a.setInt(1);
    save(a);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 2, getRevision(info));
    assertEquals(0, info.getOldGenerationCount());

    a.setInt(2);
    save(a);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 2, getRevision(info));
    assertEquals(0, info.getOldGenerationCount());

    fObjectStore.endUpdate();

    assertEquals(Constants.INITIAL_HEAD_REVISION + 3, getRevision(info));
    assertEquals(1, info.getOldGenerationCount());

    reopen();
    IntObject a_l1 = fObjectStore.get(a_id);
    info = fObjectStore.getObjectInfo(a_l1);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 3, getRevision(info));
    assertEquals(1, info.getOldGenerationCount());
  }

  public void test_version_for_no_change() {
    IntObject a = new IntObject(0);
    IObjectId a_id = save(a);
    a = null;

    reopen();

    IntObject a_l1 = fObjectStore.get(a_id);
    IObjectInfo info = fObjectStore.getObjectInfo(a_l1);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 2, getRevision(info));
    assertEquals(0, info.getOldGenerationCount());
  }

}
