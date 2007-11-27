package bootstrap.examples.query;

import java.lang.reflect.Method;
import java.util.*;

import org.memoriadb.exception.MemoriaException;

import bootstrap.examples.query.subb.Y;

public class Main {
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    Object x = new Y().getX();
    x.getClass();

    List list = new ArrayList<Number>();
    ArrayList<Object> s = (ArrayList<Object>) list;
        
    query(String.class);
    
    query(new IFilter<String>() {

      @Override
      public boolean include(String obj) {
        // TODO Auto-generated method stub
        return false;
      }
      
    });

    
    System.out.println("run");
    
  }
  
  public static <T extends CharSequence> void query(Class<T> t) {
    query(new IFilter<T>() {

      @Override
      public boolean include(T obj) {
        return true;
      }
      
    });
    
  }
  
  public static <T> Set<T> query(IFilter<T> filter) {
    Method method = findMethod(filter.getClass());
    Class<?> type = method.getParameterTypes()[0];
    System.out.println(type.getName());
    
    return null;
  }
  
  private static Method findMethod(Class<?> klazz) {
    for(Method method: klazz.getMethods()) {
      if(!method.getName().equals("include")) continue;
      if(method.getParameterTypes().length != 1 ) continue;
      return method;
    }
    throw new MemoriaException("assumed to be unreachable");    
  }

}
