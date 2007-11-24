package org.memoriadb.core.query;

import java.util.List;

import org.memoriadb.IFilter;
import org.memoriadb.core.IObjectRepository;

public interface IQueryStrategy {
  
  public <T> List<T> getAll(IObjectRepository objectRepository, Class<T> clazz);
  
  public <T> List<T> getAll(IObjectRepository objectRepository, Class<T> clazz, IFilter<T> filter);
  
  public List<Object> getAll(IObjectRepository objectRepository, String clazz);

  public List<Object> getAll(IObjectRepository objectRepository, String clazz, IFilter<Object> filter);

}
