package org.memoriadb.test.handler.collection;

import java.util.*;

import org.memoriadb.id.IObjectId;
import org.memoriadb.test.handler.testclassses.HashSetKey;


public class HashSetTest extends SetTest {

  public void test_key_with_reference() {
    Set<HashSetKey> set1 = new HashSet<HashSetKey>();
    Set<HashSetKey> set2 = new HashSet<HashSetKey>();
    
    final int count = 10;
    
    for(int i = 0; i < count; ++i) {
      set1.add(new HashSetKey(Integer.toString(i)));
      set2.add(new HashSetKey(Integer.toString(i)));
    }
    
    IObjectId id1 = saveAll(set1);
    IObjectId id2 = saveAll(set2);
    
    reopen();
    
    set1 = get(id1);
    set2 = get(id2);
    
    assertEquals(count, set1.size());
    assertEquals(count, set2.size());
  }
  
  @Override
  protected <T> Collection<T> createCollection() {
    return new HashSet<T>();
  }
  

}
