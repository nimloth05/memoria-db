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

package org.memoriadb.testutil;

import junit.framework.Assert;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CollectionUtil {

  /**
   * @param expected
   * @param actual
   */
  public static void assertIterable(Iterable<?> expected, Iterable<?> actual) {
    Iterator<?> expectations = expected.iterator();
    Iterator<?> actuals = actual.iterator();

    while (expectations.hasNext()) {
      Assert.assertTrue("not enough actuals: " + expected + " != " + actual, actuals.hasNext());

      Object obj1 = expectations.next();
      Object obj2 = actuals.next();

      if (obj1.getClass().isArray()) {
        Assert.assertTrue(Arrays.deepEquals((Object[]) obj1, (Object[]) obj2));
        continue;
      }

      Assert.assertEquals(obj1, obj2);
    }

    Assert.assertFalse("too many actuals: " + expected + " != " + actual, actuals.hasNext());
  }

  /**
   * Asserts that all expected objects are contained in the actual collection. No order is considered.
   */
  public static void containsAll(Iterable<?> actual, Object... expected) {
    Set<Object> set = new HashSet<Object>();
    for (Object o : actual) {
      set.add(o);
    }

    for (Object obj : expected) {
      Assert.assertTrue("Missing: " + obj, set.remove(obj));
    }

    Assert.assertTrue("Unexpected objects: " + set, set.isEmpty());
  }

  @SuppressWarnings("unused")
  // mein das "obj" in der for-schleife.
  public static int count(Iterable<?> iterable) {
    int i = 0;
    for (Object obj : iterable)
      ++i;
    return i;
  }

}
