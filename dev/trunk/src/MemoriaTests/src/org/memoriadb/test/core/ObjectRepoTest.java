/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.test.core;

import junit.framework.TestCase;

import org.memoriadb.core.*;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.handler.field.ReflectionHandlerFactory;
import org.memoriadb.id.IObjectId;
import org.memoriadb.id.loong.*;
import org.memoriadb.test.testclasses.*;
import org.memoriadb.testutil.CollectionUtil;

public class ObjectRepoTest extends TestCase {
  
  private ObjectRepository fRepo;
  
  public void test_deleted_object_is_not_contained() {
    StringObject obj = new StringObject();
    IObjectId id = fRepo.add(obj, new LongId(1)).getId();
    
    fRepo.contains(id);
    fRepo.contains(obj);
    
    fRepo.delete(obj);
    
    assertFalse(fRepo.contains(obj));
    assertFalse(fRepo.contains(id));
  }
  
  public void test_getAllUserSpaceObjects() {
    IMemoriaClass classObject = ReflectionHandlerFactory.createNewType(StringObject.class, new LongId(1));
    fRepo.add(new LongId(2), classObject);
    
    assertEquals(1, CollectionUtil.count(fRepo.getAllObjects()));
    assertEquals(0, CollectionUtil.count(fRepo.getAllUserSpaceObjects()));
    
    fRepo.add(new StringObject(), new LongId(3));
    
    assertEquals(2, CollectionUtil.count(fRepo.getAllObjects()));
    assertEquals(1, CollectionUtil.count(fRepo.getAllUserSpaceObjects()));
  }
  
  public void test_getAllUserSpaceObjects_by_class() {
    IMemoriaClass classObject = ReflectionHandlerFactory.createNewType(StringObject.class, new LongId(1));
    fRepo.add(new LongId(2), classObject);
    fRepo.add(new StringObject(), new LongId(3));
    fRepo.add(new IntObject(1), new LongId(3));
    
    assertEquals(1, CollectionUtil.count(fRepo.getAllUserSpaceObjects(StringObject.class)));
    assertEquals(2, CollectionUtil.count(fRepo.getAllUserSpaceObjects(Object.class)));
    assertEquals(0, CollectionUtil.count(fRepo.getAllUserSpaceObjects(IMemoriaClass.class)));
  }
  
  public void test_put_meta_object_in_cache() {
    IMemoriaClass classObject = ReflectionHandlerFactory.createNewType(StringObject.class, fRepo.getIdFactory().getFieldMetaClass());
    IObjectId id = fRepo.add(classObject, classObject.getMemoriaClassId()).getId();
    
    assertSame(classObject, fRepo.getExistingObject(id));
    assertSame(classObject, fRepo.getMemoriaClass(StringObject.class.getName()));
  }
  
  public void test_put_meta_object_with_id_in_cache() {
    IMemoriaClass classObject = ReflectionHandlerFactory.createNewType(StringObject.class, new LongId(1));
    IObjectId id = new LongId(20);
    fRepo.handleAdd(new ObjectInfo(id, new LongId(1), classObject, 0, 0));
    
    assertSame(classObject, fRepo.getExistingObject(id));
    assertSame(classObject, fRepo.getMemoriaClass(StringObject.class.getName()));
  }
  
  public void test_put_new_object_in_cache() {
    StringObject obj = new StringObject();
    IObjectId id = fRepo.add(obj, new LongId(1)).getId();
    Object obj2 = fRepo.getExistingObject(id);
    assertSame(obj, obj2);
  }
  
  public void test_put_object_with_id_in_cache() {
    StringObject obj = new StringObject();
    //Wir starten hier absichtlich mit 20.
    IObjectId objectId = new LongId(20);
    fRepo.handleAdd(new ObjectInfo(objectId, new LongId(1), obj, 0, 0));
    
    StringObject obj2 = new StringObject();
    IObjectId id = fRepo.add(obj2, new LongId(1)).getId();
    assertEquals(new LongId(21), id);
    
    assertEquals(obj, fRepo.getExistingObject(objectId));
    assertEquals(obj2, fRepo.getExistingObject(id));
  }
  
  public void test_try_double_registration_of_a_memoria_class() {
    fRepo.add(new LongId(2), ReflectionHandlerFactory.createNewType(StringObject.class, new LongId(1)));
    try {
      fRepo.add(new LongId(2), ReflectionHandlerFactory.createNewType(StringObject.class, new LongId(1)));
      fail("double registration of a Memoria-Class is not allowed");
    }
    catch (MemoriaException e) {
      //passed
    }
  }
  
  public void test_try_double_registration_of_object() {
    StringObject stringObject = new StringObject();
    fRepo.add(stringObject, new LongId(1));
    try {
      fRepo.add(stringObject, new LongId(2));
      fail("Double registration for the same object is not allowed.");
    } 
    catch (MemoriaException e) {
      //We passed
    }
  }
  
  public void test_try_double_use_of_id() {
    IObjectId id = new LongId(1);
    fRepo.handleAdd(new ObjectInfo(id, new LongId(2), new StringObject()));
    try {
      fRepo.handleAdd(new ObjectInfo(id, new LongId(2), new StringObject()));
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
  