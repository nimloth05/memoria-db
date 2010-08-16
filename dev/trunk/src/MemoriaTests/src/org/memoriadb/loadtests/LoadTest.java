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

package org.memoriadb.loadtests;

import org.memoriadb.TestMode;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.Referencer;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.testutil.AbstractMemoriaTest;

import java.util.ArrayList;
import java.util.List;

public class LoadTest extends AbstractMemoriaTest {
  
  @SuppressWarnings("nls")
  public void test_save_objectref() throws Exception {
    List<Object> objects = new ArrayList<Object>();
    
    beginUpdate();
    
    for(int i = 0; i < 30000; ++i) {
      Referencer composite = new Referencer();
      composite.set(StringObject.class, "1");
      objects.add(composite);
      saveAll(composite);
    }
    
    endUpdate();
    
    List<Referencer> allSavedObjects = query(Referencer.class);
    for(Referencer ref: allSavedObjects) {
      Object obj = ref.get();
      IObjectId objectId = fObjectStore.getId(obj);
      assertSame("id collision: "+objectId, obj, fObjectStore.get(objectId));
    }
    
    reopen();
    
    Referencer composite = query(Referencer.class).get(0);
    assertNotNull("1", composite.getStringValueFromReferencee());
  }
  
  public void test_save_thentousends_obejcts() {
    fObjectStore.beginUpdate();
    
    for(int i = 0; i < 10000; ++i) {
      save(new Object());
    }
    
    long currentTime = System.nanoTime();
    fObjectStore.endUpdate();

    long durationInMs = (System.nanoTime() - currentTime) / 1000000;
    assertTrue("10'000 took " +durationInMs, durationInMs < 500);
  }

  @Override
  protected TestMode getTestMode() {
    return TestMode.filesystem;
  }
  
}
