package org.memoriadb;


public interface IFilter<T> {
  
  /**
   * @return true to add the given <tt>object</tt> to the result-set.
   */
  public boolean accept(T object,  IFilterControl control);
  
}
