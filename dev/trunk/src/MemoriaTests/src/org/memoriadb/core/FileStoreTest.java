package org.memoriadb.core;

import java.util.List;

import org.memoriadb.core.testclasses.*;

import bootstrap.core.*;

public class FileStoreTest extends AbstractFileStoreTest {
  
  public void test_hydration() {
    save(new A(1, "a1"), new A(2, "a2"));
    
    reopen();
  }
  
  public void test_object_ref() {
    Referencer referencer = new Referencer();
    referencer.set("object_ref");
    save(referencer.get(), referencer);
    
    assertEquals(1, getAll(TestObj.class).size());
    
    reopen();
    Referencer loadedReferencer = getAll(Referencer.class).get(0);
    
    assertEquals(referencer.get().getString(), loadedReferencer.get().getString());
    
    TestObj loadedObj = getAll(TestObj.class).get(0);
    assertSame(loadedReferencer.get(), loadedObj);
  }
  
  public void test_save_referencee_before_referencer_is_saved() {
    Referencer referencer = new Referencer();
    referencer.set("1");
    save(referencer.get());
    save(referencer);
    
    reopen();
    Referencer loadedReferencer = getAll(Referencer.class).get(0);
    
    assertEquals(referencer.get().getString(), loadedReferencer.get().getString());
    
    TestObj loadedObj = getAll(TestObj.class).get(0);
    assertSame(loadedReferencer.get(), loadedObj);
  }
  
  public void test_save_two_objects_in_two_transactions() {
    TestObj obj1 = new TestObj("1", 1);
    TestObj obj2 = new TestObj("2", 2);
    
    save(obj1);
    save(obj2);
    
    reopen();
    
    List<TestObj> objs = getAll(TestObj.class);
    assertEquals(obj1.getString(), objs.get(0).getString());
    assertEquals(obj2.getString(), objs.get(1).getString());
  }
  
  public void test_svae_object_ref() {
    Referencer referencer = new Referencer();
    referencer.set("1");
    save(referencer);
    
    reopen();
    
    Referencer loadedReferencer = getAll(Referencer.class).get(0);
    assertEquals(referencer.get().getString(), loadedReferencer.get().getString());
    
    TestObj loadedObj = getAll(TestObj.class).get(0);
    assertSame(loadedReferencer.get(), loadedObj);
  }
  
}
