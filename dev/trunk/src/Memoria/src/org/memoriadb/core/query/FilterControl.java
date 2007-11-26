package org.memoriadb.core.query;

import java.util.*;

import org.memoriadb.IFilterControl;

public class FilterControl implements IFilterControl {

  private static final int INITIAL_SIZE = 32;
  
  private final List<Object> fResult = new ArrayList<Object>(INITIAL_SIZE);
  private boolean fAbort = false;
  
  @Override
  public void abort() {
    fAbort = true;
  }
  
  public void add(Object object){
    fResult.add(object);
  }

  @Override
  public int getCount() {
    return fResult.size();
  }
  
  public List<Object> getResult() {
    return fResult;
  }

  public boolean isAbort() {
    return fAbort;
  }

}
