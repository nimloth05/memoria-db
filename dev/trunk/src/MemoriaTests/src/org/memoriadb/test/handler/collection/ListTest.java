package org.memoriadb.test.handler.collection;

import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.testutil.CollectionUtil;

import java.util.List;


public abstract class ListTest extends CollectionTest {
  
  public void test_preserve_order_with_mixed_content() {
    List<Object> list = (List<Object>) createCollection();
    
    for(int i = 0; i < 10; ++i) {
      list.add(new StringObject(Integer.toString(i)));
      list.add(i*3);
    }
    
    IObjectId id = saveAll(list);
    
    reopen();
    
    List<Object> l1_list = get(id);
    CollectionUtil.assertIterable(list, l1_list);
  }
  
  public void test_preserve_order_with_object() {
    List<Object> list = (List<Object>) createCollection();
    
    for(int i = 0; i < 10; ++i) {
      list.add(new StringObject(Integer.toString(i)));
    }
    
    IObjectId id = saveAll(list);
    
    reopen();
    
    List<Object> l1_list = get(id);
    CollectionUtil.assertIterable(list, l1_list);
  }
  
  public void test_preserve_order_with_primitive() {
    List<Object> list = (List<Object>) createCollection();
    
    for(int i = 0; i < 10; ++i) {
      list.add(i*3);
    }
    
    IObjectId id = save(list);
    reopen();
    
    List<Object> l1_list = get(id);
    CollectionUtil.assertIterable(list, l1_list);
  }

}
