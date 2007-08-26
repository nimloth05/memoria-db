package org.memoriadb;

public interface IFilter<T> {
  
  public boolean accept(T object);

}
