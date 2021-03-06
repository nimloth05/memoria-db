/*
 * Copyright 2010 memoria db projet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.memoriadb.test.handler.enu;

import org.memoriadb.handler.enu.EnumObject;
import org.memoriadb.handler.enu.IEnumObject;
import org.memoriadb.handler.field.IFieldbasedObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.ObjectReferencer;
import org.memoriadb.test.testclasses.enums.EnumUse;
import org.memoriadb.test.testclasses.enums.ObjectEnumUse;
import org.memoriadb.test.testclasses.enums.TestEnum;
import org.memoriadb.testutil.AbstractMemoriaTest;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractEnumTest extends AbstractMemoriaTest {

  public void test_add_enum_class_adds_all_instances() {
    fObjectStore.getTypeInfo().addMemoriaClassIfNecessary(TestEnum.class);
    assertAllEnumInstanceAreContained();
    reopen();
    assertAllEnumInstanceAreContained();
  }

  public void test_add_enum_throws_in_dataMode() {
    reopenDataMode();

    try {
      fDataStore.save(new EnumObject(TestEnum.a));
      fail("enum can only be added via the IRefactor-interface");
    }
    catch (Exception e) {
      // pass
    }

  }

  /**
   * enum is aggregated from a field ob type Object.
   */
  public void test_aggregated_enum() {
    ObjectReferencer obj = new ObjectReferencer(TestEnum.a);
    IObjectId id = save(obj);
    
    reopen();
    
    assertNotSame(obj, get(id));
    obj = get(id);
    
    assertSame(TestEnum.a, obj.getObject());
  }

  public void test_data_mode() {
    EnumUse obj = new EnumUse();
    obj.setEnum(TestEnum.b);
    IObjectId objId = saveAll(obj);
    obj = null;

    reopenDataMode();

    IFieldbasedObject l1_obj = fDataStore.get(objId);
    IEnumObject l1_enumObjet = (IEnumObject) l1_obj.get("fEnum");
    assertEquals(TestEnum.b.name(), l1_enumObjet.getName());
  }

  public void test_deleteAll_does_not_remove_enum() {
    EnumUse e1 = new EnumUse(TestEnum.c);

    saveAll(e1);

    deleteAll(e1);

    reopen();

    assertAllEnumInstanceAreContained();
  }

  public void test_derived_enum() {
    EnumUse obj = new EnumUse();
    obj.setEnum(TestEnum.c);
    IObjectId id = saveAll(obj);

    reopen();

    EnumUse l1_obj = get(id);
    assertEquals(TestEnum.c, l1_obj.getEnum());
  }

  public void test_enum_array() {
    TestEnum[] enumArray = new TestEnum[2];
    enumArray[0] = TestEnum.a;
    enumArray[1] = TestEnum.c;
    IObjectId id = saveAll(enumArray);

    reopen();
    TestEnum[] l1_enumArray = get(id);
    assertTrue(Arrays.deepEquals(enumArray, l1_enumArray));
  }

  public void test_enum_set() {
    EnumSet<TestEnum> enumSet = EnumSet.noneOf(TestEnum.class);
    enumSet.add(TestEnum.a);
    enumSet.add(TestEnum.b);
    IObjectId id = saveAll(enumSet);

    reopen();
    EnumSet<TestEnum> l1_enumSet = get(id);
    assertEquals(2, l1_enumSet.size());

    l1_enumSet.remove(TestEnum.a);
    save(l1_enumSet);

    reopen();

    EnumSet<TestEnum> l2_enumSet = get(id);
    assertEquals(1, l2_enumSet.size());
  }

  public void test_field() {
    EnumUse obj = new EnumUse();
    obj.setEnum(TestEnum.b);
    IObjectId objId = saveAll(obj);
    obj = null;

    reopen();
    EnumUse l1_obj = fObjectStore.get(objId);
    assertEquals(TestEnum.b, l1_obj.getEnum());
  }

  public void test_null_ref() {
    EnumUse obj = new EnumUse();
    obj.setEnum(null);
    IObjectId objId = save(obj);
    obj = null;

    reopen();
    EnumUse l1_obj = fObjectStore.get(objId);
    assertEquals(null, l1_obj.getEnum());
  }

  public void test_save_adds_enum_if_necessary() {
    IObjectId id = save(new EnumUse(TestEnum.c));

    reopen();

    EnumUse enumUse = get(id);
    assertEquals(TestEnum.c, enumUse.getEnum());
    assertEquals(3, fObjectStore.query(TestEnum.class).size());
  }

  public void test_save_enum_is_not_possible() {
    try {
      save(TestEnum.a);
      fail("it is not possible to explicitly save an enum-instance");
    }
    catch (Exception e) {
      // pass
    }

    try {
      save(TestEnum.c);
      fail("it is not possible to explicitly save an enum-instance");
    }
    catch (Exception e) {
      // pass
    }
  }

  public void test_scenario() {
    EnumUse obj = new EnumUse();
    obj.setEnum(null);
    IObjectId objId = save(obj);
    obj = null;

    reopen();
    EnumUse l1_obj = fObjectStore.get(objId);
    assertEquals(null, l1_obj.getEnum());

    l1_obj.setEnum(TestEnum.a);
    saveAll(l1_obj);

    reopen();
    EnumUse l2_obj = fObjectStore.get(objId);
    assertEquals(l1_obj.getEnum(), l2_obj.getEnum());

    l2_obj.setEnum(TestEnum.b);
    saveAll(l2_obj);

    reopen();
    EnumUse l3_obj = fObjectStore.get(objId);
    assertEquals(l2_obj.getEnum(), l3_obj.getEnum());
  }

  public void test_use_enum_in_a_object_field() {
    ObjectEnumUse obj = new ObjectEnumUse();
    obj.setEnum(TestEnum.b);
    IObjectId id = saveAll(obj);

    reopen();

    ObjectEnumUse l1_obj = get(id);
    assertEquals(TestEnum.b, l1_obj.getEnum());
  }
  
  public void test_use_enum_list() {
    List<TestEnum> list = new LinkedList<TestEnum>();
    list.add(TestEnum.a);
    IObjectId id = saveAll(list);

    reopen();
    List<Object> l1_list = get(id);
    assertEquals(3, fObjectStore.query(TestEnum.class).size());
    assertFalse(l1_list.isEmpty());
    assertEquals(TestEnum.a, l1_list.get(0));
  }

  private void assertAllEnumInstanceAreContained() {
    for(TestEnum enumInstance: TestEnum.values()) {
      assertTrue(fObjectStore.contains(enumInstance));
    }
  }

}
