package org.memoriadb.test.core;

import java.util.ArrayList;

import org.memoriadb.ITypeInfo;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.test.core.testclasses.SimpleTestObj;
import org.memoriadb.test.core.testclasses.inheritance.C;
import org.memoriadb.testutil.AbstractObjectStoreTest;

public class TypeInfoTest extends AbstractObjectStoreTest {
  
  public void test_add_class() {
    beginUpdate();
    IObjectId id = fObjectStore.getTypeInfo().addMemoriaClass(SimpleTestObj.class);
    endUpdate();
    
    IMemoriaClass memoriaClass = fObjectStore.get(id);
    
    assertEquals(id, typeInfo().getMemoriaClassId(SimpleTestObj.class));
    assertEquals(memoriaClass, typeInfo().getMemoriaClass(SimpleTestObj.class));
    
    reopen();
    
    assertEquals(id, typeInfo().getMemoriaClassId(SimpleTestObj.class));
    
    reopenDataMode();
    
    assertEquals(id, fDataStore.getTypeInfo().getMemoriaClassId(SimpleTestObj.class));
  }
  
  public void test_add_memoria_class() {
    int startCount = getMemoriaClassCount();

    addClass(Object.class);
    addClass(Object[].class);
    addClass(ArrayList.class);
    
    assertEquals(startCount, getMemoriaClassCount());
    
    // plus 1 class
    addClass(SimpleTestObj.class);
    assertEquals(startCount+1, getMemoriaClassCount());
    
    // plus 3 classes from type hierarchy
    addClass(C[][][][].class);
    assertEquals(startCount+4, getMemoriaClassCount());

  }
  
  public void test_class_is_not_found() {
    assertNull(typeInfo().getMemoriaClass(SimpleTestObj.class));
    assertNull(typeInfo().getMemoriaClass(new SimpleTestObj()));
    assertNull(typeInfo().getMemoriaClassId(SimpleTestObj.class));
    assertNull(typeInfo().getMemoriaClassId(new SimpleTestObj()));
  }
  
  public void test_primitives_can_not_be_added() {
    checkAddFails(int.class);
    checkAddFails(int[][].class);
    checkAddFails(Integer.class);
    checkAddFails(String.class);
  }
  
  public void test_TypeInfo() {
    assertEquals(get(typeInfo().getMemoriaClassId(Object.class)), typeInfo().getMemoriaClass(Object.class));
    assertEquals(Object.class.getName(), typeInfo().getMemoriaClass(Object.class).getJavaClassName());

    assertEquals(get(typeInfo().getMemoriaClassId(ArrayList.class)), typeInfo().getMemoriaClass(ArrayList.class));
    assertEquals(ArrayList.class.getName(), typeInfo().getMemoriaClass(ArrayList.class).getJavaClassName());

    Object obj = new Object();
    save(obj);
    assertEquals(get(typeInfo().getMemoriaClassId(obj)), typeInfo().getMemoriaClass(obj));
    assertEquals(Object.class.getName(), typeInfo().getMemoriaClass(obj).getJavaClassName());

    ArrayList<String> list = new ArrayList<String>();
    save(list);
    assertEquals(get(typeInfo().getMemoriaClassId(list)), typeInfo().getMemoriaClass(list));
    assertEquals(ArrayList.class.getName(), typeInfo().getMemoriaClass(list).getJavaClassName());
  }

  
  private void addClass(Class<?> klass) {
    fObjectStore.getTypeInfo().addMemoriaClass(klass);
  }

  private void checkAddFails(Class<?> clazz) {
    try {
      addClass(clazz);
      fail("exception expected");
    }
    catch(MemoriaException e) {
      
    }
  }

  private int getMemoriaClassCount() {
    return query(IMemoriaClass.class).size();
  }

  private ITypeInfo typeInfo() {
    return fObjectStore.getTypeInfo();
  }
  
}

