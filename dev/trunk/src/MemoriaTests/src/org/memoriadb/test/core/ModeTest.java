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

import org.memoriadb.handler.field.FieldbasedDataObject;
import org.memoriadb.handler.field.IFieldbasedObject;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class ModeTest extends AbstractMemoriaTest {

  public void test_add_object() {
    StringObject obj = new StringObject("1");
    IObjectId id1 = save(obj);
    
    IObjectId memoriaClassId = fObjectStore.getTypeInfo().getMemoriaClassId(obj);
    
    reopenDataMode();
    
    IFieldbasedObject obj2 = new FieldbasedDataObject(memoriaClassId);
    obj2.set("fString", fDataStore.getRefactorApi().getLangValueObject("2"));
    IObjectId id2 = save(obj2);
    
    reopen();
    
    assertEquals(2, query(StringObject.class).size());
    
    obj = get(id1);
    assertEquals("1", obj.getString());

    obj = get(id2);
    assertEquals("2", obj.getString());

  }
  
  public void test_delete_object_in_data_mode() {
    StringObject obj = new StringObject("1");
    IObjectId id = save(obj);
    
    reopenDataMode();
    
    fDataStore.delete(fDataStore.get(id));
    
    reopen();
    
    assertFalse(fObjectStore.containsId(id));
  }

  public void test_save_obj() {
    StringObject obj = new StringObject("1");
    IObjectId id = save(obj);
    
    reopenDataMode();
    
    IFieldbasedObject l1_obj = fDataStore.get(id);
    assertTrue(l1_obj.equalsLangValueObject("fString", obj.getString()));
  }
  
  public void test_update_obj() {
    StringObject obj = new StringObject("1");
    IObjectId id = save(obj);
    
    reopenDataMode();
    
    //We get a special object, because we are in the data mode
    IFieldbasedObject l1_obj = fDataStore.get(id);
    l1_obj.set("fString", fDataStore.getRefactorApi().getLangValueObject("2"));
    save(l1_obj);
    
    reopen();
    
    StringObject l2_obj = fObjectStore.get(id);
    l1_obj.equalsLangValueObject("fString", l2_obj.getString());
  }
}
