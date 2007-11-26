package org.memoriadb;

public interface IFilterControl {
  
  /**
   * Stops iterating, leaving the result-set untouched.
   */
  public void abort();
  
  /**
   * @return Number of Objects already added to the result-set.
   */
  public int getCount();
  
}
