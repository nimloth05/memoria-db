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

package org.memoriadb.test.crud.delete;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.ObjectReferencer;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.testutil.AbstractMemoriaTest;

/**
 * The reason for this Test is to verify that the error-Message is detailed enough in case an referenced object was
 * deleted.
 *
 */
public class DeleteReferencedObjectTest extends AbstractMemoriaTest {

  public void test_error_message() {
    StringObject referencee = new StringObject("StringObject");
    ObjectReferencer referencer = new ObjectReferencer(referencee);

    IObjectId id = saveAll(referencer);
    delete(referencee);
    
    System.out.println(fObjectStore.getId(referencer));
    System.out.println(referencer.getObject());

    try {
      reopen();
      //fail("reopen() should throw because the referencee was deleted.");
    }
    catch(MemoriaException e) {
      System.out.println(e.getMessage());
    }
    
    referencer = fObjectStore.get(id);
    System.out.println(referencer.getObject());

  }

}
