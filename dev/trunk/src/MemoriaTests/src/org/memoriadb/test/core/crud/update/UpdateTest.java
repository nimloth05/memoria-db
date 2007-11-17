package org.memoriadb.test.core.crud.update;

import org.memoriadb.core.IObjectInfo;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.testclasses.OneInt;
import org.memoriadb.testutil.AbstractObjectStoreTest;
import org.memoriadb.util.Constants;

public abstract class UpdateTest extends AbstractObjectStoreTest {

  public void test_basic_revision_handling() {
    Object o1 = new Object();
    IObjectId id1 = save(o1);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 2, getObjectInfo(id1).getRevision());

    Object o2 = new Object();
    IObjectId id2 = save(o2);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 3, getObjectInfo(id2).getRevision());

    delete(o1);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 4, getObjectInfo(id1).getRevision());
    assertEquals(Constants.INITIAL_HEAD_REVISION + 3, getObjectInfo(id2).getRevision());
    
    reopen();
    
    assertEquals(Constants.INITIAL_HEAD_REVISION + 4, getObjectInfo(id1).getRevision());
    assertEquals(Constants.INITIAL_HEAD_REVISION + 3, getObjectInfo(id2).getRevision());
  }

  public void test_revision_for_change_on_original_and_on_l1() {
    OneInt a = new OneInt(0);
    IObjectId a_id = save(a);
    IObjectInfo info = fStore.getObjectInfo(a);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 2, info.getRevision());
    assertEquals(0, info.getOldGenerationCount());

    a.setInt(1);
    save(a);

    assertEquals(Constants.INITIAL_HEAD_REVISION + 3, info.getRevision());
    assertEquals(1, info.getOldGenerationCount());
    a = null;

    reopen();

    OneInt a_l1 = fStore.getObject(a_id);
    info = fStore.getObjectInfo(a_l1);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 3, info.getRevision());
    assertEquals(1, info.getOldGenerationCount());

    a_l1.setInt(2);
    save(a_l1);
    a_l1 = null;
    assertEquals(Constants.INITIAL_HEAD_REVISION + 4, info.getRevision());
    assertEquals(2, info.getOldGenerationCount());

    reopen();

    OneInt a_l2 = fStore.getObject(a_id);
    info = fStore.getObjectInfo(a_l2);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 4, info.getRevision());
    assertEquals(2, info.getOldGenerationCount());
  }

  public void test_version_for_many_changes_in_one_transaction_on_original() {
    OneInt a = new OneInt(0);
    IObjectId a_id = save(a);
    IObjectInfo info = fStore.getObjectInfo(a);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 2, info.getRevision());
    assertEquals(0, info.getOldGenerationCount());

    fStore.beginUpdate();

    a.setInt(1);
    save(a);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 2, info.getRevision());
    assertEquals(0, info.getOldGenerationCount());

    a.setInt(2);
    save(a);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 2, info.getRevision());
    assertEquals(0, info.getOldGenerationCount());

    fStore.endUpdate();

    assertEquals(Constants.INITIAL_HEAD_REVISION + 3, info.getRevision());
    assertEquals(1, info.getOldGenerationCount());

    reopen();
    OneInt a_l1 = fStore.getObject(a_id);
    info = fStore.getObjectInfo(a_l1);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 3, info.getRevision());
    assertEquals(1, info.getOldGenerationCount());
  }

  public void test_version_for_no_change() {
    OneInt a = new OneInt(0);
    IObjectId a_id = save(a);
    a = null;

    reopen();

    OneInt a_l1 = fStore.getObject(a_id);
    IObjectInfo info = fStore.getObjectInfo(a_l1);
    assertEquals(Constants.INITIAL_HEAD_REVISION + 2, info.getRevision());
    assertEquals(0, info.getOldGenerationCount());
  }

}
