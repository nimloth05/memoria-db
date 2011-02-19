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

package org.memoriadb.test.handler.jdk.date;

import java.util.*;

import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.ObjectReferencer;
import org.memoriadb.testutil.AbstractMemoriaTest;

public class DateHandlerTest extends AbstractMemoriaTest {
  
  public void testSaveDate() {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.set(Calendar.HOUR_OF_DAY, 1);
    calendar.set(Calendar.MINUTE, 1);
    ObjectReferencer references = new ObjectReferencer();
    references.setObject(calendar.getTime());
    final IObjectId id = saveAll(references);
    
    reopen();
    
    ObjectReferencer l1_referencer = get(id);
    Date date = (Date) l1_referencer.getObject();
    GregorianCalendar calendar2 = new GregorianCalendar();
    calendar2.setTime(date);
    
    assertEquals(calendar.get(Calendar.HOUR_OF_DAY), calendar2.get(Calendar.HOUR_OF_DAY));
    assertEquals(calendar.get(Calendar.MINUTE), calendar2.get(Calendar.MINUTE));
    
  }
}
