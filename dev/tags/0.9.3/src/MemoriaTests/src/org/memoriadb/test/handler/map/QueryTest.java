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

package org.memoriadb.test.handler.map;

import org.memoriadb.testutil.AbstractMemoriaTest;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class QueryTest extends AbstractMemoriaTest {
  public void test() {
    save(new HashMap<Object,Object>());
    save(new ConcurrentHashMap<Object,Object>());
    
    assertEquals(2, fObjectStore.query(AbstractMap.class).size());
  }
}
