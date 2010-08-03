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

package org.memoriadb.test.handler.collection;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

public class CopyOnWriteArraySetTest extends SetTest {

  @Override
  public void test_list_in_list() {
    // sets in sets cannot be serialized
  }
  
  @Override
  public void test_list_in_list_in_multiple_save_calls() {
    // sets in sets cannot be serialized    
  }
  
  @Override
  protected <T> Collection<T> createCollection() {
    return new CopyOnWriteArraySet<T>();
  }

}
