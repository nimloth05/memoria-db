/*
 * Copyright 2010 Micha Riser
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

import org.memoriadb.core.ObjectsByClassIndex;
import org.memoriadb.testutil.*;

public class ObjectsByClassIndexTest extends AbstractMemoriaTest {

  public class ClassA implements I3 {}
  public class ClassB implements I4 {}
  public class ClassC extends ClassA {}
  public interface I1 {};

  public interface I2 {}

  public interface I3 extends I2 {}

  public interface I4 extends I1, I2 {}

  private ObjectsByClassIndex fIndex;
  private ClassA fA1;
  private ClassA fA2;
  private ClassB fB1;
  private ClassB fB2;
  private ClassC fC1;
  private ClassC fC2;
  
  public void test_add_and_get() {
    fIndex.add(fA1);
    fIndex.add(fB1);
    
    CollectionUtil.containsAll(fIndex.getObjects(I1.class), fB1);
    CollectionUtil.containsAll(fIndex.getObjects(I2.class), fA1,fB1);
    CollectionUtil.containsAll(fIndex.getObjects(I3.class), fA1);
    CollectionUtil.containsAll(fIndex.getObjects(I4.class), fB1);
    CollectionUtil.containsAll(fIndex.getObjects(ClassA.class), fA1);
    CollectionUtil.containsAll(fIndex.getObjects(ClassB.class), fB1);
    CollectionUtil.containsAll(fIndex.getObjects(ClassC.class));
    CollectionUtil.containsAll(fIndex.getObjects(Object.class), fA1, fB1);
    CollectionUtil.containsAll(fIndex.getClassesWithRegisteredObjects(), ClassA.class, ClassB.class);
    
    
    fIndex.add(fA2);
    fIndex.add(fC1);
    
    CollectionUtil.containsAll(fIndex.getObjects(I1.class), fB1);
    CollectionUtil.containsAll(fIndex.getObjects(I2.class), fA1,fA2,fB1,fC1);
    CollectionUtil.containsAll(fIndex.getObjects(I3.class), fA1,fA2,fC1);
    CollectionUtil.containsAll(fIndex.getObjects(I4.class), fB1);
    CollectionUtil.containsAll(fIndex.getObjects(ClassA.class), fA1,fA2,fC1);
    CollectionUtil.containsAll(fIndex.getObjects(ClassB.class), fB1);
    CollectionUtil.containsAll(fIndex.getObjects(ClassC.class), fC1);
    CollectionUtil.containsAll(fIndex.getObjects(Object.class), fA1, fB1, fA2, fC1);
    CollectionUtil.containsAll(fIndex.getClassesWithRegisteredObjects(), ClassA.class, ClassB.class, ClassC.class);
    
    fIndex.add(fB2);
    fIndex.add(fC2);
    
    CollectionUtil.containsAll(fIndex.getObjects(I1.class), fB1, fB2);
    CollectionUtil.containsAll(fIndex.getObjects(I2.class), fA1,fA2,fB1,fB2,fC1,fC2);
    CollectionUtil.containsAll(fIndex.getObjects(I3.class), fA1,fA2,fC1,fC2);
    CollectionUtil.containsAll(fIndex.getObjects(I4.class), fB1,fB2);
    CollectionUtil.containsAll(fIndex.getObjects(ClassA.class), fA1,fA2,fC1,fC2);
    CollectionUtil.containsAll(fIndex.getObjects(ClassB.class), fB1,fB2);
    CollectionUtil.containsAll(fIndex.getObjects(ClassC.class), fC1,fC2);
    CollectionUtil.containsAll(fIndex.getObjects(Object.class), fA1, fB1, fA2, fC1, fB2, fC2);
    CollectionUtil.containsAll(fIndex.getClassesWithRegisteredObjects(), ClassA.class, ClassB.class, ClassC.class);
  }
  
  public void test_empty_index() {
    assertEquals(0, count(fIndex.getObjects(I1.class)));
    assertEquals(0, count(fIndex.getObjects(I2.class)));
    assertEquals(0, count(fIndex.getObjects(I3.class)));
    assertEquals(0, count(fIndex.getObjects(I4.class)));
    assertEquals(0, count(fIndex.getObjects(ClassA.class)));
    assertEquals(0, count(fIndex.getObjects(ClassB.class)));
    assertEquals(0, count(fIndex.getObjects(ClassC.class)));
    assertEquals(0, count(fIndex.getClassesWithRegisteredObjects()));
  }
  
  public void test_remove() {
    fIndex.add(fA1);
    fIndex.add(fA2);
    fIndex.add(fB1);
    fIndex.add(fB2);
    fIndex.add(fC1);
    fIndex.add(fC2);
    
    CollectionUtil.containsAll(fIndex.getObjects(I1.class), fB1, fB2);
    CollectionUtil.containsAll(fIndex.getObjects(I2.class), fA1,fA2,fB1,fB2,fC1,fC2);
    CollectionUtil.containsAll(fIndex.getObjects(I3.class), fA1,fA2,fC1,fC2);
    CollectionUtil.containsAll(fIndex.getObjects(I4.class), fB1,fB2);
    CollectionUtil.containsAll(fIndex.getObjects(ClassA.class), fA1,fA2,fC1,fC2);
    CollectionUtil.containsAll(fIndex.getObjects(ClassB.class), fB1,fB2);
    CollectionUtil.containsAll(fIndex.getObjects(ClassC.class), fC1,fC2);
    CollectionUtil.containsAll(fIndex.getObjects(Object.class), fA1, fB1, fA2, fC1, fB2, fC2);
    CollectionUtil.containsAll(fIndex.getClassesWithRegisteredObjects(), ClassA.class, ClassB.class, ClassC.class);

    fIndex.remove(fB2);
    fIndex.remove(fC2);
    
    CollectionUtil.containsAll(fIndex.getObjects(I1.class), fB1);
    CollectionUtil.containsAll(fIndex.getObjects(I2.class), fA1,fA2,fB1,fC1);
    CollectionUtil.containsAll(fIndex.getObjects(I3.class), fA1,fA2,fC1);
    CollectionUtil.containsAll(fIndex.getObjects(I4.class), fB1);
    CollectionUtil.containsAll(fIndex.getObjects(ClassA.class), fA1,fA2,fC1);
    CollectionUtil.containsAll(fIndex.getObjects(ClassB.class), fB1);
    CollectionUtil.containsAll(fIndex.getObjects(ClassC.class), fC1);
    CollectionUtil.containsAll(fIndex.getObjects(Object.class), fA1, fB1, fA2, fC1);
    CollectionUtil.containsAll(fIndex.getClassesWithRegisteredObjects(), ClassA.class, ClassB.class, ClassC.class);
   
    fIndex.remove(fA2);
    fIndex.remove(fC1);
    
    
    CollectionUtil.containsAll(fIndex.getObjects(I1.class), fB1);
    CollectionUtil.containsAll(fIndex.getObjects(I2.class), fA1,fB1);
    CollectionUtil.containsAll(fIndex.getObjects(I3.class), fA1);
    CollectionUtil.containsAll(fIndex.getObjects(I4.class), fB1);
    CollectionUtil.containsAll(fIndex.getObjects(ClassA.class), fA1);
    CollectionUtil.containsAll(fIndex.getObjects(ClassB.class), fB1);
    CollectionUtil.containsAll(fIndex.getObjects(ClassC.class));
    CollectionUtil.containsAll(fIndex.getObjects(Object.class), fA1, fB1);
    CollectionUtil.containsAll(fIndex.getClassesWithRegisteredObjects(), ClassA.class, ClassB.class);
    
    fIndex.remove(fA1);
    fIndex.remove(fB1);
    
    assertEquals(0, count(fIndex.getObjects(I1.class)));
    assertEquals(0, count(fIndex.getObjects(I2.class)));
    assertEquals(0, count(fIndex.getObjects(I3.class)));
    assertEquals(0, count(fIndex.getObjects(I4.class)));
    assertEquals(0, count(fIndex.getObjects(ClassA.class)));
    assertEquals(0, count(fIndex.getObjects(ClassB.class)));
    assertEquals(0, count(fIndex.getObjects(ClassC.class)));
    assertEquals(0, count(fIndex.getClassesWithRegisteredObjects()));
  }

  @Override
  protected void setUp() {
    super.setUp();
    fIndex = new ObjectsByClassIndex();
    fA1 = new ClassA();
    fA2 = new ClassA();
    fB1 = new ClassB();
    fB2 = new ClassB();
    fC1 = new ClassC();
    fC2 = new ClassC();
  }
  
  private Object count(Iterable<?> objects) {
    return CollectionUtil.count(objects);
  }

}
