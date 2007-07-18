package bootstrap.core;

import bootstrap.core.testclasses.FieldTypeTestClass;
import junit.framework.TestCase;

public class FieldTypeTest extends TestCase {
  
  @SuppressWarnings("nls")
  public void test_type() throws SecurityException, NoSuchFieldException {
    FieldTypeTestClass instance = new FieldTypeTestClass();
    
    Class<? extends FieldTypeTestClass> class1 = instance.getClass();
    assertEquals(FieldType.booleanPrimitive, FieldType.getType(class1.getDeclaredField("fBooleanP")));
    assertEquals(FieldType.booleanPrimitive, FieldType.getType(class1.getDeclaredField("fBooleanC")));
    
    assertEquals(FieldType.charPrimitive, FieldType.getType(class1.getDeclaredField("fCharP")));
    assertEquals(FieldType.charPrimitive, FieldType.getType(class1.getDeclaredField("fCharC")));
    
    assertEquals(FieldType.typePrimitive, FieldType.getType(class1.getDeclaredField("fByteC")));
    assertEquals(FieldType.typePrimitive, FieldType.getType(class1.getDeclaredField("fByteC")));
    
    assertEquals(FieldType.shortPrimitive, FieldType.getType(class1.getDeclaredField("fShortC")));
    assertEquals(FieldType.shortPrimitive, FieldType.getType(class1.getDeclaredField("fShortC")));
    
    assertEquals(FieldType.integerPrimitive, FieldType.getType(class1.getDeclaredField("fIntC")));
    assertEquals(FieldType.integerPrimitive, FieldType.getType(class1.getDeclaredField("fIntC")));
    
    assertEquals(FieldType.longPrimitive, FieldType.getType(class1.getDeclaredField("fLongC")));
    assertEquals(FieldType.longPrimitive, FieldType.getType(class1.getDeclaredField("fLongC")));
    
    assertEquals(FieldType.floatPrimitive, FieldType.getType(class1.getDeclaredField("fFloatC")));
    assertEquals(FieldType.floatPrimitive, FieldType.getType(class1.getDeclaredField("fFloatC")));
    
    assertEquals(FieldType.doublePrimitive, FieldType.getType(class1.getDeclaredField("fDoubleC")));
    assertEquals(FieldType.doublePrimitive, FieldType.getType(class1.getDeclaredField("fDoubleC")));
    
    assertEquals(FieldType.string, FieldType.getType(class1.getDeclaredField("fString")));
    
    assertEquals(FieldType.clazz, FieldType.getType(class1.getDeclaredField("fObject")));
  }
  

}
