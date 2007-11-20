package org.memoriadb.core.query;

import java.util.List;

import org.memoriadb.IFilter;
import org.memoriadb.core.IObjectRepo;

public interface IQueryStrategy {
  
  public <T> List<T> getAll(IObjectRepo objectRepo, Class<T> clazz);
  
  public <T> List<T> getAll(IObjectRepo objectRepo, Class<T> clazz, IFilter<T> filter);
  
  public List<Object> getAll(IObjectRepo objectRepo, String clazz);

  public List<Object> getAll(IObjectRepo objectRepo, String clazz, IFilter<Object> filter);

}
