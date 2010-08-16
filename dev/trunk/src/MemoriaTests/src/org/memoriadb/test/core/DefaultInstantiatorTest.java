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

package org.memoriadb.test.core;

import junit.framework.TestCase;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.instantiator.DefaultInstantiator;
import org.memoriadb.instantiator.IInstantiator;
import org.memoriadb.test.testclasses.ctor.NoDefault;
import org.memoriadb.test.testclasses.ctor.Private;
import org.memoriadb.test.testclasses.ctor.Protected;

public class DefaultInstantiatorTest extends TestCase {
  
  private IInstantiator fInstantiator;
  
  public void test_cannot_instantiate_object_with_no_default_ctor() {
    try {
      fInstantiator.checkCanInstantiateObject(NoDefault.class.getName());
      fail("exception expected");
    }
    catch (Exception e) {
      //passed
    }
    
    try {
      fInstantiator.newInstance(NoDefault.class.getName());
      fail("Can not instantiate an Object with no default ctor");
    } 
    catch(MemoriaException e) {
      //passed
    }
  }
  
  public void test_instantiate_object_with_private_ctor() throws Exception {
    fInstantiator.checkCanInstantiateObject(Private.class.getName());
    assertNotNull(fInstantiator.newInstance(Private.class.getName()));
  }
  
  public void test_instantiate_object_with_protected_ctor() throws Exception {
    fInstantiator.checkCanInstantiateObject(Protected.class.getName());
    assertNotNull(fInstantiator.newInstance(Protected.class.getName()));
  }
    
  
  @Override
  protected void setUp() {
    fInstantiator = new DefaultInstantiator();
  }

}
