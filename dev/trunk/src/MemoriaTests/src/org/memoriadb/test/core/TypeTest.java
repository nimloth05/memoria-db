package org.memoriadb.test.core;

import junit.framework.TestCase;

import org.memoriadb.core.meta.Type;
import org.memoriadb.test.testclasses.FieldTypeTestClass;

public class TypeTest extends TestCase {
  
  @SuppressWarnings("nls")
  public void test_type() throws Exception {
    FieldTypeTestClass obj = new FieldTypeTestClass();
    
    Class<? extends FieldTypeTestClass> class1 = obj.getClass();
    assertEquals(Type.typeBoolean, Type.getType(obj.getBooleanFieldP()));
    assertEquals(Type.typeBooleanC, Type.getType(obj.getBooleanFieldC()));
    
    assertEquals(Type.typeChar, Type.getType(obj.getCharFieldP()));
    assertEquals(Type.typeCharC, Type.getType(obj.getCharFieldC()));
    
    assertEquals(Type.typeByte, Type.getType(obj.getByteFieldP()));
    assertEquals(Type.typeByteC, Type.getType(obj.getByteFieldC()));
    
    assertEquals(Type.typeShort, Type.getType(obj.getShortFieldP()));
    assertEquals(Type.typeShortC, Type.getType(obj.getShortFieldC()));
    
    assertEquals(Type.typeInteger, Type.getType(obj.getIntFieldP()));
    assertEquals(Type.typeIntegerC, Type.getType(obj.getIntFieldC()));
    
    assertEquals(Type.typeLong, Type.getType(obj.getLongFieldP()));
    assertEquals(Type.typeLongC, Type.getType(obj.getLongFieldC()));
    
    assertEquals(Type.typeFloat, Type.getType(obj.getFloatFieldP()));
    assertEquals(Type.typeFloatC, Type.getType(obj.getFloatFieldC()));
    
    assertEquals(Type.typeDouble, Type.getType(obj.getDoubleFieldP()));
    assertEquals(Type.typeDoubleC, Type.getType(obj.getDoubleFieldC()));
    
    assertEquals(Type.typeString, Type.getType(class1.getDeclaredField("fString")));
    
    assertEquals(Type.typeClass, Type.getType(class1.getDeclaredField("fObject")));
  }
  
}
