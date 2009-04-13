package org.memoriadb.test.core;

import java.util.ArrayList;

import org.memoriadb.ITypeInfo;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.field.IFieldbasedObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.*;
import org.memoriadb.test.testclasses.inheritance.*;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class TypeInfoTest extends AbstractMemoriaTest {
  
  public void test_add_class() {
    beginUpdate();
    IObjectId id = fObjectStore.getTypeInfo().addMemoriaClassIfNecessary(SimpleTestObj.class);
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
    addClass(Object.class);
    addClass(Object[].class);
    addClass(ArrayList.class);
    
    // plus 1 class
    addClass(SimpleTestObj.class);
    assertNotNull(typeInfo().getMemoriaClass(SimpleTestObj.class));
    
    // plus 3 classes from type hierarchy
    addClass(C[][][][].class);
    
    assertNotNull(typeInfo().getMemoriaClass(A.class));
    assertNotNull(typeInfo().getMemoriaClass(B.class));
    assertNotNull(typeInfo().getMemoriaClass(C.class));
    
    assertTypeHierachy(C.class);
  }
  
  public void test_class_is_not_found() {
    assertNull(typeInfo().getMemoriaClass(SimpleTestObj.class));
    assertNull(typeInfo().getMemoriaClass(new SimpleTestObj()));
    assertNull(typeInfo().getMemoriaClassId(SimpleTestObj.class));
    assertNull(typeInfo().getMemoriaClassId(new SimpleTestObj()));
  }
  
  /**
   * Tests in object- and data-mode.
   */
  public void test_getMemoriaClass_for_value_object() {
    ObjectReferencer ref = new ObjectReferencer(new ValueObjectReferencer());
    IObjectId id = save(ref);
    
    IMemoriaClass clazz = fObjectStore.getTypeInfo().getMemoriaClass(ref.getObject());
    assertEquals(ValueObjectReferencer.class.getName(), clazz.getJavaClassName());
    
    reopenDataMode();
    
    IFieldbasedObject l1_ref = fDataStore.get(id);
    IFieldbasedObject vor = (IFieldbasedObject) l1_ref.get("fObject");
    assertNull(vor.get("fObject"));
    
    clazz = fDataStore.getTypeInfo().getMemoriaClass(vor);
    assertEquals(ValueObjectReferencer.class.getName(), clazz.getJavaClassName());
  }
  
  public void test_primitives_can_not_be_added() {
    checkAddFails(int.class);
    checkAddFails(int[][].class);
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
    fObjectStore.getTypeInfo().addMemoriaClassIfNecessary(klass);
  }

  private void checkAddFails(Class<?> clazz) {
    try {
      addClass(clazz);
      fail("exception expected");
    }
    catch(MemoriaException e) {
      
    }
  }

  private ITypeInfo typeInfo() {
    return fObjectStore.getTypeInfo();
  }
  
}

