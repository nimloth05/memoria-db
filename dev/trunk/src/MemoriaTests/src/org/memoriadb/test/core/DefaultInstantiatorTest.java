package org.memoriadb.test.core;

import junit.framework.TestCase;

import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.instantiator.*;
import org.memoriadb.test.testclasses.ctor.*;

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
