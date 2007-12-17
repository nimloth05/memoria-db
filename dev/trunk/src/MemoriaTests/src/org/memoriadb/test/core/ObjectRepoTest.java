package org.memoriadb.test.core;

import junit.framework.TestCase;

import org.memoriadb.block.Block;
import org.memoriadb.core.*;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.meta.*;
import org.memoriadb.handler.field.FieldbasedMemoriaClass;
import org.memoriadb.id.IObjectId;
import org.memoriadb.id.loong.*;
import org.memoriadb.test.testclasses.SimpleTestObj;
import org.memoriadb.testutil.CollectionUtil;

public class ObjectRepoTest extends TestCase {
  
  private ObjectRepository fRepo;
  
  public void test_deleted_object_is_not_contained() {
    SimpleTestObj obj = new SimpleTestObj();
    IObjectId id = fRepo.add(obj, new LongId(1)).getId();
    
    fRepo.contains(id);
    fRepo.contains(obj);
    
    fRepo.delete(obj);
    
    assertFalse(fRepo.contains(obj));
    assertFalse(fRepo.contains(id));
  }
  
  public void test_getAllUserSpaceObjects() {
    IMemoriaClass classObject = new FieldbasedMemoriaClass(SimpleTestObj.class, new LongId(1));
    fRepo.add(new LongId(2), classObject);
    
    assertEquals(1, CollectionUtil.count(fRepo.getAllObjects()));
    assertEquals(0, CollectionUtil.count(fRepo.getAllUserSpaceObjects()));
    
    fRepo.add(new SimpleTestObj(), new LongId(3));
    
    assertEquals(2, CollectionUtil.count(fRepo.getAllObjects()));
    assertEquals(1, CollectionUtil.count(fRepo.getAllUserSpaceObjects()));
  }
  
  public void test_put_meta_object_in_cache() {
    IMemoriaClass classObject = new FieldbasedMemoriaClass(SimpleTestObj.class, fRepo.getIdFactory().getFieldMetaClass());
    IObjectId id = fRepo.add(classObject, classObject.getMemoriaClassId()).getId();
    
    assertSame(classObject, fRepo.getExistingObject(id));
    assertSame(classObject, fRepo.getMemoriaClass(SimpleTestObj.class.getName()));
  }
  
  public void test_put_meta_object_with_id_in_cache() {
    IMemoriaClass classObject = new FieldbasedMemoriaClass(SimpleTestObj.class, new LongId(1));
    IObjectId id = new LongId(20);
    fRepo.handleAdd(new ObjectInfo(id, new LongId(1), classObject, Block.getDefaultBlock(), 0, 0));
    
    assertSame(classObject, fRepo.getExistingObject(id));
    assertSame(classObject, fRepo.getMemoriaClass(SimpleTestObj.class.getName()));
  }
  
  
  public void test_put_new_object_in_cache() {
    SimpleTestObj obj = new SimpleTestObj();
    IObjectId id = fRepo.add(obj, new LongId(1)).getId();
    Object obj2 = fRepo.getExistingObject(id);
    assertSame(obj, obj2);
  }
  
  public void test_put_object_with_id_in_cache() {
    SimpleTestObj obj = new SimpleTestObj();
    //Wir starten hier absichtlich mit 20.
    IObjectId objectId = new LongId(20);
    fRepo.handleAdd(new ObjectInfo(objectId, new LongId(1), obj, Block.getDefaultBlock(), 0, 0));
    
    SimpleTestObj obj2 = new SimpleTestObj();
    IObjectId id = fRepo.add(obj2, new LongId(1)).getId();
    assertEquals(new LongId(21), id);
    
    assertEquals(obj, fRepo.getExistingObject(objectId));
    assertEquals(obj2, fRepo.getExistingObject(id));
  }
  
  public void test_try_double_registration_of_a_memoria_class() {
    fRepo.add(new LongId(2), new FieldbasedMemoriaClass(SimpleTestObj.class, new LongId(1)));
    try {
      fRepo.add(new LongId(2), new FieldbasedMemoriaClass(SimpleTestObj.class, new LongId(1)));
      fail("double registration of a Memoria-Class is not allowed");
    }
    catch (MemoriaException e) {
      //passed
    }
  }
  
  public void test_try_double_registration_of_object() {
    SimpleTestObj simpleTestObj = new SimpleTestObj();
    fRepo.add(simpleTestObj, new LongId(1));
    try {
      fRepo.add(simpleTestObj, new LongId(2));
      fail("Double registration for the same object is not allowed.");
    } 
    catch (MemoriaException e) {
      //We passed
    }
  }
  
  public void test_try_double_use_of_id() {
    IObjectId id = new LongId(1);
    fRepo.handleAdd(new ObjectInfo(id, new LongId(2), new SimpleTestObj(), Block.getDefaultBlock()));
    try {
      fRepo.handleAdd(new ObjectInfo(id, new LongId(2), new SimpleTestObj(), Block.getDefaultBlock()));
      fail("Double use of an objectId is not allowed.");
    } 
    catch (MemoriaException e) {
      //We passed
    }
  }
  
  @Override
  protected void setUp() {
    fRepo = new ObjectRepository(new LongIdFactory());
  }
  
}
  