package org.memoriadb.test.handler.collection;

import java.util.List;

import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.SimpleTestObj;
import org.memoriadb.testutil.CollectionUtil;


public abstract class ListTest extends CollectionTest {
  
  public void test_preserve_order_with_mixed_content() {
    List<Object> list = (List<Object>) createCollection();
    
    for(int i = 0; i < 10; ++i) {
      list.add(new SimpleTestObj(Integer.toString(i)));
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
      list.add(new SimpleTestObj(Integer.toString(i)));
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
