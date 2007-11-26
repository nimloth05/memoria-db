package bootstrap.examples.query;

public interface IFilter<T> {
  
  public boolean include(T obj);
}
