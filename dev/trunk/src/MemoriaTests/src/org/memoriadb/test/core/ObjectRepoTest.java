package org.memoriadb.test.core;

import junit.framework.TestCase;

import org.memoriadb.block.Block;
import org.memoriadb.core.*;
import org.memoriadb.core.meta.*;
import org.memoriadb.id.IObjectId;
import org.memoriadb.id.loong.*;
import org.memoriadb.test.core.testclasses.SimpleTestObj;

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
  
  @Override
  protected void setUp() {
    fRepo = new ObjectRepository(new LongIdFactory());
  }
  
}
 