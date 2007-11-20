package org.memoriadb.test.core;

import junit.framework.TestCase;

import org.memoriadb.core.*;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.test.core.testclasses.ctor.*;

public class DefaultInstantiatorTest extends TestCase {
  
  private IDefaultInstantiator fDefaultInstantiator;
  
  public void test_can_not_instantiate_object_with_no_default_ctor() {
    assertFalse(fDefaultInstantiator.canInstantiateObject(NoDefault.class.getName()));
    try {
      fDefaultInstantiator.newInstance(NoDefault.class.getName());
      fail("Can not instantiate an Object with no default ctor");
    } 
    catch(MemoriaException e) {
      //passed
    }
  }
  
  public void test_instantiate_object_with_private_ctor() {
    assertTrue(fDefaultInstantiator.canInstantiateObject(Private.class.getName()));
    assertNotNull(fDefaultInstantiator.newInstance(Private.class.getName()));
  }
  
  public void test_instantiate_object_with_protected_ctor() {
    assertTrue(fDefaultInstantiator.canInstantiateObject(Protected.class.getName()));
    assertNotNull(fDefaultInstantiator.newInstance(Protected.class.getName()));
  }
    
  
  @Override
  protected void setUp() {
    fDefaultInstantiator = new DefaultDefaultInstantiator();
  }

}
