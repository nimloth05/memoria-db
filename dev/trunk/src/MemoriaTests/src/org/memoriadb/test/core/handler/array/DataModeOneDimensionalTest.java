package org.memoriadb.test.core.handler.array;

import org.memoriadb.core.handler.IDataObject;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public class DataModeOneDimensionalTest extends AbstractObjectStoreTest {
  
  public void test_open_int_array() {
    int[] arr = new int[]{1,2};
    IObjectId id = save(arr);
    
    reopenDataMode();
    
    IDataObject x = fDataStore.getObject(id);
    
  }
  
}
