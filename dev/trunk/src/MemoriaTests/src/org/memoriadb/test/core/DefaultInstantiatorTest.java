package org.memoriadb.test.core;

import junit.framework.TestCase;

import org.memoriadb.core.*;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.test.core.testclasses.ctor.*;

public class DefaultInstantiatorTest extends TestCase {
  
  private IInstantiator fInstantiator;
  
  public void test_can_not_instantiate_object_with_no_default_ctor() {
    assertFalse(fInstantiator.canInstantiateObject(NoDefault.class.getName()));
    try {
      fInstantiator.newInstance(NoDefault.class.getName());
      fail("Can not instantiate an Object with no default ctor");
    } 
    catch(MemoriaException e) {
      //passed
    }
  }
  
  public void test_instantiate_object_with_private_ctor() {
    assertTrue(fInstantiator.canInstantiateObject(Private.class.getName()));
    assertNotNull(fInstantiator.newInstance(Private.class.getName()));
  }
  
  public void test_instantiate_object_with_protected_ctor() {
    assertTrue(fInstantiator.canInstantiateObject(Protected.class.getName()));
    assertNotNull(fInstantiator.newInstance(Protected.class.getName()));
  }
    
  
  @Override
  protected void setUp() {
    fInstantiator = new DefaultInstantiator();
  }

}
