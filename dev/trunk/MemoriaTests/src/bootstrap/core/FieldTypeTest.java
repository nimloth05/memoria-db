package bootstrap.core;

import bootstrap.core.classes.FieldTypeTestClass;
import junit.framework.TestCase;

public class FieldTypeTest extends TestCase {
  
  @SuppressWarnings("nls")
  public void test_type() throws SecurityException, NoSuchFieldException {
    FieldTypeTestClass instance = new FieldTypeTestClass();
    
    Class<? extends FieldTypeTestClass> class1 = instance.getClass();
    assertEquals(FieldType.bool, FieldType.getType(class1.getDeclaredField("fBooleanP")));
    assertEquals(FieldType.bool, FieldType.getType(class1.getDeclaredField("fBooleanC")));
    
    assertEquals(FieldType.chr, FieldType.getType(class1.getDeclaredField("fCharP")));
    assertEquals(FieldType.chr, FieldType.getType(class1.getDeclaredField("fCharC")));
    
    assertEquals(FieldType.bte, FieldType.getType(class1.getDeclaredField("fByteC")));
    assertEquals(FieldType.bte, FieldType.getType(class1.getDeclaredField("fByteC")));
    
    assertEquals(FieldType.shrt, FieldType.getType(class1.getDeclaredField("fShortC")));
    assertEquals(FieldType.shrt, FieldType.getType(class1.getDeclaredField("fShortC")));
    
    assertEquals(FieldType.integer, FieldType.getType(class1.getDeclaredField("fIntC")));
    assertEquals(FieldType.integer, FieldType.getType(class1.getDeclaredField("fIntC")));
    
    assertEquals(FieldType.lng, FieldType.getType(class1.getDeclaredField("fLongC")));
    assertEquals(FieldType.lng, FieldType.getType(class1.getDeclaredField("fLongC")));
    
    assertEquals(FieldType.flot, FieldType.getType(class1.getDeclaredField("fFloatC")));
    assertEquals(FieldType.flot, FieldType.getType(class1.getDeclaredField("fFloatC")));
    
    assertEquals(FieldType.duble, FieldType.getType(class1.getDeclaredField("fDoubleC")));
    assertEquals(FieldType.duble, FieldType.getType(class1.getDeclaredField("fDoubleC")));
    
    assertEquals(FieldType.string, FieldType.getType(class1.getDeclaredField("fString")));
    
    assertEquals(FieldType.clazz, FieldType.getType(class1.getDeclaredField("fObject")));
  }
  

}
