/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package bootstrap.examples.query;

import bootstrap.examples.query.subb.Y;
import org.memoriadb.core.exception.MemoriaException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
