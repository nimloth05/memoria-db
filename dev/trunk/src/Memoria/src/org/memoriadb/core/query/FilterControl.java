package org.memoriadb.core.query;

import java.util.*;

import org.memoriadb.IFilterControl;

public class FilterControl<T> implements IFilterControl {

  private static final int INITIAL_SIZE = 32;
  
  private final List<T> fResult = new ArrayList<T>(INITIAL_SIZE);
  private boolean fAbort = false;
  
  @Override
  public void abort() {
    fAbort = true;
  }
  
  public void add(T object){
    fResult.add(object);
  }

  @Override
  public int getCount() {
    return fResult.size();
  }
  
  public List<T> getResult() {
    return fResult;
  }

  public boolean isAbort() {
    return fAbort;
  }

}
