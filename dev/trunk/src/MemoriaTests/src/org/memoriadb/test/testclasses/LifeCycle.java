package org.memoriadb.test.testclasses;

import org.memoriadb.ILifeCycle;

public class LifeCycle implements ILifeCycle {

  private int fReconstituteCounter = 0;

  public int getReconstituteCounter() {
    return fReconstituteCounter;
  }

  @Override
  public void postReconstitute() {
    ++fReconstituteCounter;
  }
  
  
}
