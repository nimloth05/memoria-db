package org.memoriadb.test.core;

import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.LifeCycle;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class LifeCycleTest extends AbstractMemoriaTest {
  
  public void test_postReconstitute() {
    LifeCycle lc = new LifeCycle();
    
    IObjectId id = save(lc);
    
    assertEquals(0, lc.getReconstituteCounter());
    
    reopen();
    
    assertEquals(0, lc.getReconstituteCounter());
    
    lc = get(id);
    
    assertEquals(1, lc.getReconstituteCounter());
    
    
  }
  
}
