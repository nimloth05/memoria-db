package org.memoriadb.test.crud.valueobject;

import java.util.*;

import org.memoriadb.CreateConfig;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.crud.valueobject.testclasses.AnnotatedObjectReferencer;
import org.memoriadb.test.testclasses.*;
import org.memoriadb.test.testclasses.composite.*;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class ValueObjectTest extends AbstractMemoriaTest {
  
  public void test_ArrayList() {
    List<Object> list = new ArrayList<Object>();
    list.add(new Object());
    ObjectReferencer ref = new ObjectReferencer(list);
    
    IObjectId id = save(ref);

    
    reopen();
    
    ref = get(id);
    list = (List<Object>) ref.getObject();
    
    assertNotNull(list.get(0));
  }
  
  /**
   * ObjectReferencer->ValueObjectReferencer->ArrayList
   */
  public void test_ArrayList_in_ValueObject() {
    ValueObjectReferencer valueRef = new ValueObjectReferencer();
    valueRef.setObject(new ArrayList<Object>());
    ObjectReferencer ref = new ObjectReferencer(valueRef);
    
    IObjectId id = save(ref);
    
    reopen();
    
    ref = get(id);
    valueRef = (ValueObjectReferencer) ref.getObject();
    ArrayList<Object> list = (ArrayList<Object>) valueRef.getObject();
    
    assertTrue(fObjectStore.contains(ref));
    assertFalse(fObjectStore.contains(valueRef));
    assertFalse(fObjectStore.contains(list));
  }
  
  public void test_Composite() {
    IComponent c = new Composite();
    c.addChild(new Composite());
    c.addChild(new Leaf());
    
    ObjectReferencer ref = new ObjectReferencer(c);
    
    // save is enough, because the whole composite is stored inline
    IObjectId id = save(ref);
    
    assertEquals(1, fObjectStore.query(Object.class).size());
    
    reopen();
    
    assertEquals(1, fObjectStore.query(Object.class).size());
    
    ref = get(id);
    c = (IComponent) ref.getObject();
    assertEquals(2, c.getChildCount());
    
  }
  
  public void test_isValueObject_flag_is_saved() {
    assertTrue(fObjectStore.getTypeInfo().getMemoriaClass(ArrayList.class).isValueObject());
    assertFalse(fObjectStore.getTypeInfo().getMemoriaClass(LinkedList.class).isValueObject());
    assertTrue(fObjectStore.getTypeInfo().getMemoriaClass(Object.class).isValueObject());
    assertTrue(fObjectStore.getTypeInfo().getMemoriaClass(Composite.class).isValueObject());
    assertTrue(fObjectStore.getTypeInfo().getMemoriaClass(Leaf.class).isValueObject());
    
    reopen();

    assertTrue(fObjectStore.getTypeInfo().getMemoriaClass(ArrayList.class).isValueObject());
    assertFalse(fObjectStore.getTypeInfo().getMemoriaClass(LinkedList.class).isValueObject());
    assertTrue(fObjectStore.getTypeInfo().getMemoriaClass(Object.class).isValueObject());
    assertTrue(fObjectStore.getTypeInfo().getMemoriaClass(Composite.class).isValueObject());
    assertTrue(fObjectStore.getTypeInfo().getMemoriaClass(Leaf.class).isValueObject());
  }
  
  public void test_Object_is_stored_inline() {
    ObjectReferencer ref = new ObjectReferencer(new Object());
    IObjectId id = save(ref);
    assertTrue(fObjectStore.contains(ref));
    assertFalse(fObjectStore.contains(ref.getObject()));
    assertEquals(1, fObjectStore.query(Object.class).size());
    
    reopen();
    
    ref = get(id);
    assertTrue(fObjectStore.contains(ref));
    assertFalse(fObjectStore.contains(ref.getObject()));
    assertEquals(1, fObjectStore.query(Object.class).size());
    assertNotNull(ref.getObject());
  }
  
  public void test_ValueObject_in_ValueObjecct() {
    AnnotatedObjectReferencer aor = new AnnotatedObjectReferencer();
    ArrayList<Object> list = new ArrayList<Object>();
    aor.setObject(list);
    
    ObjectReferencer ref = new ObjectReferencer(aor);
    
    // save is enough, because the whole composite is stored inline
    IObjectId id = save(ref);
    
    assertTrue(fObjectStore.contains(ref));
    assertFalse(fObjectStore.contains(ref.getObject()));
    
    reopen();

    ref = get(id);
    assertTrue(fObjectStore.contains(ref));
    assertFalse(fObjectStore.contains(ref.getObject()));

    
  }

  @Override
  protected void configureOpen(CreateConfig config) {
    config.addValueClass(ArrayList.class);
    config.addValueClass(Object.class);
    config.addValueClass(Composite.class);
    config.addValueClass(Leaf.class);
    config.addValueClass(AnnotatedObjectReferencer.class);
  }
  
}
