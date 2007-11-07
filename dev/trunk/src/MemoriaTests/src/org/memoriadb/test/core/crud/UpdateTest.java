package org.memoriadb.test.core.crud;

import org.memoriadb.core.IObjectInfo;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.test.core.testclasses.OneInt;
import org.memoriadb.testutil.AbstractObjectStoreTest;
import org.memoriadb.util.Constants;

public class UpdateTest extends AbstractObjectStoreTest {
  
  public void test_version_for_change_on_original_and_on_l1() {
    OneInt a = new OneInt(0);
    IObjectId a_id = save(a);
    IObjectInfo info = fStore.getObjectInfo(a);
    assertEquals(Constants.INITIAL_VERSION, info.getVersion());
    assertEquals(0, info.getOldGenerationCount());
    
    a.setInt(1);
    save(a);
    
    assertEquals(Constants.INITIAL_VERSION+1, info.getVersion());
    assertEquals(1, info.getOldGenerationCount());
    a = null;
    
    reopen();
    
    OneInt a_l1 = fStore.getObject(a_id);
    info = fStore.getObjectInfo(a_l1);
    assertEquals(Constants.INITIAL_VERSION+1, info.getVersion());
    assertEquals(1, info.getOldGenerationCount());
    
    a_l1.setInt(2);
    save(a_l1);
    a_l1=null;
    assertEquals(Constants.INITIAL_VERSION+2, info.getVersion());
    assertEquals(2, info.getOldGenerationCount());
    
    reopen();
    
    OneInt a_l2 = fStore.getObject(a_id);
    info = fStore.getObjectInfo(a_l2);
    assertEquals(Constants.INITIAL_VERSION+2, info.getVersion());
    assertEquals(2, info.getOldGenerationCount());
  }

  
  public void test_version_for_many_changes_in_one_transaction_on_original() {
    OneInt a = new OneInt(0);
    IObjectId a_id = save(a);
    IObjectInfo info = fStore.getObjectInfo(a);
    assertEquals(Constants.INITIAL_VERSION, info.getVersion());
    assertEquals(0, info.getOldGenerationCount());
    
    fStore.beginUpdate();
    
    a.setInt(1);
    save(a);
    assertEquals(Constants.INITIAL_VERSION, info.getVersion());
    assertEquals(0, info.getOldGenerationCount());
    
    a.setInt(2);
    save(a);
    assertEquals(Constants.INITIAL_VERSION, info.getVersion());
    assertEquals(0, info.getOldGenerationCount());
    
    fStore.endUpdate();
    
    assertEquals(Constants.INITIAL_VERSION+1, info.getVersion());
    assertEquals(1, info.getOldGenerationCount());
    
    reopen();
    OneInt a_l1 = fStore.getObject(a_id);
    info = fStore.getObjectInfo(a_l1);
    assertEquals(Constants.INITIAL_VERSION+1, info.getVersion());
    assertEquals(1, info.getOldGenerationCount());
  }
  
  public void test_version_for_no_change() {
    OneInt a = new OneInt(0);
    IObjectId a_id = save(a);
    a=null;
    
    reopen();
    
    OneInt a_l1 = fStore.getObject(a_id);
    IObjectInfo info = fStore.getObjectInfo(a_l1);
    assertEquals(Constants.INITIAL_VERSION, info.getVersion());
    assertEquals(0, info.getOldGenerationCount());
  }
  
}
