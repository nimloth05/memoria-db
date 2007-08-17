package org.memoriadb.test.util;

import java.util.*;

import junit.framework.TestCase;

import org.memoriadb.test.core.testclasses.WrongHashCode;
import org.memoriadb.util.IdentityHashSet;

public class IdentityHashSetTest extends TestCase {
  
  public void test_add_remove() {
    Set<WrongHashCode> set = new IdentityHashSet<WrongHashCode>();
    
    assertTrue( set.add(new WrongHashCode()) );
    
    WrongHashCode obj = new WrongHashCode();
    assertTrue(  set.add(obj) );
    assertFalse( set.add(obj) );
    
    assertEquals(2, set.size());
  }
  
  public void test_addAll() {
    List<WrongHashCode> add = new ArrayList<WrongHashCode>();
    add.add(new WrongHashCode("1"));
    
    Set<WrongHashCode> set = new IdentityHashSet<WrongHashCode>();
    set.add(new WrongHashCode("4"));
    
    assertTrue(set.addAll(add));
    assertEquals(2, set.size());
  }
  
  public void test_clear() {
    Set<WrongHashCode> set = new IdentityHashSet<WrongHashCode>();
    
    set.add(new WrongHashCode());
    set.clear();
    assertEquals(0, set.size());
  }
  
  public void test_contains() {
    Set<WrongHashCode> set = new IdentityHashSet<WrongHashCode>();
    
    set.add(new WrongHashCode());
    assertFalse(set.contains(new WrongHashCode()));
  }
  
  public void test_containsAll() {
    List<WrongHashCode> contain = new ArrayList<WrongHashCode>();
    contain.add(new WrongHashCode("1"));
    
    Set<WrongHashCode> set = new IdentityHashSet<WrongHashCode>();
    set.add(new WrongHashCode("4"));
    
    assertFalse(set.containsAll(contain));
  }
  
  public void test_iterator() {
    Set<WrongHashCode> set = new IdentityHashSet<WrongHashCode>();
    WrongHashCode expected = new WrongHashCode(); 
    set.add(expected);
    Iterator<WrongHashCode> iterator = set.iterator();
    assertTrue(iterator.hasNext());
    assertSame(expected, iterator.next());
  }
  
  public void test_remove() {
    Set<WrongHashCode> set = new IdentityHashSet<WrongHashCode>();
    
    WrongHashCode obj = new WrongHashCode("1");
    WrongHashCode remain = new WrongHashCode("2");
    
    set.add(obj);
    set.add(remain);
    
    set.remove(obj);
    
    assertEquals(1, set.size());
    assertEquals(remain.getString(), set.iterator().next().getString());
  }
  
  public void test_removeAll() {
    List<WrongHashCode> removed = new ArrayList<WrongHashCode>();
    removed.add(new WrongHashCode("1"));
    
    Set<WrongHashCode> set = new IdentityHashSet<WrongHashCode>();
    set.add(new WrongHashCode("4"));
    
    set.removeAll(removed);
    assertEquals(1, set.size());
  }
  
  public void test_retainAll() {
    /*
     * There is bug in the IdentityHashMap with retainAll. It checks by equality and not by system.identityHashCode
     */
    try {
      List<WrongHashCode> retain = new ArrayList<WrongHashCode>();
      retain.add(new WrongHashCode("1"));
    
      Set<WrongHashCode> set = new IdentityHashSet<WrongHashCode>();
      set.add(new WrongHashCode("2"));
    
      assertTrue(set.retainAll(retain));
      assertEquals(0, set.size());
      
      fail("retainAll works now, rewrite test");
    } catch (UnsupportedOperationException e) {
      //passed
    }
  }
  
  
}
 