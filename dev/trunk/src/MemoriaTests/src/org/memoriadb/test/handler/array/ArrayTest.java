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

package org.memoriadb.test.handler.array;

import junit.framework.Assert;
import org.memoriadb.id.IObjectId;
import org.memoriadb.test.testclasses.StringObject;
import org.memoriadb.testutil.AbstractMemoriaTest;

import java.util.Arrays;

public class ArrayTest extends AbstractMemoriaTest {
  
  public static interface IArrayContainer {
    public void assertSame(StringObject obj);
    public void set();
  }
  
  public static class MultiDimensionalArrayContainer implements IArrayContainer {
    
    public StringObject[][] fArray = new StringObject[1][1];
    
    public MultiDimensionalArrayContainer() {}
    
    @Override
    public void assertSame(StringObject obj) {
      Assert.assertSame(obj, fArray[0][0]);
    }
    
    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      final MultiDimensionalArrayContainer other = (MultiDimensionalArrayContainer) obj;
      return Arrays.deepEquals(fArray, other.fArray);
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Arrays.hashCode(fArray);
      return result;
    }

    @Override
    public void set() {
      fArray[0][0] = new StringObject("1");
    }
  }
  
  private static class ArrayContainer implements IArrayContainer {
    
    public StringObject[] fArray = new StringObject[1];
    
    public ArrayContainer() {}
    
    @Override
    public void assertSame(StringObject obj) {
      Assert.assertSame(obj, fArray[0]);
    }
    
    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      final ArrayContainer other = (ArrayContainer) obj;
      return Arrays.equals(fArray, other.fArray);
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Arrays.hashCode(fArray);
      return result;
    }

    @Override
    public void set() {
      fArray[0] = new StringObject("1");
    }
  }
  
  public void test_multi_dimensional_array_object() {
    internal_test_array_object_container(new MultiDimensionalArrayContainer());
  }
  
  public void test_serialize_array_object() {
    internal_test_array_object_container(new ArrayContainer());
  }
  
  private void internal_test_array_object_container(IArrayContainer container) {
    container.set();

    IObjectId id = saveAll(container);
    reopen();

    IArrayContainer l1_Container = get(id);
    StringObject loadedObj = query(StringObject.class).get(0);

    l1_Container.assertSame(loadedObj);
    assertEquals(container, l1_Container);
  }

}
