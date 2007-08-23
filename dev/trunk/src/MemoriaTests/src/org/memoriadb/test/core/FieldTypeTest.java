package org.memoriadb.test.core;

import java.io.*;

import junit.framework.TestCase;

import org.easymock.*;
import org.memoriadb.core.*;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.meta.FieldType;
import org.memoriadb.test.core.testclasses.FieldTypeTestClass;

public class FieldTypeTest extends TestCase {
  
  private IReaderContext fReaderContext;
  private ISerializeContext fSerializeContext;

  public void test_read_write_field() throws Exception {
    IMocksControl control = EasyMock.createControl();
    
    DataOutput outputMock = control.createMock(DataOutput.class);
    DataInput inputMock = control.createMock(DataInput.class);
    
    FieldTypeTestClass testObj = new FieldTypeTestClass();
    
    booleanFieldTest(control, outputMock, inputMock, testObj);
    charFieldTest(control, outputMock, inputMock, testObj);
    byteFieldTest(control, outputMock, inputMock, testObj);
    shortFieldTest(control, outputMock, inputMock, testObj);
    intFieldTest(control, outputMock, inputMock, testObj);
    longFieldTest(control, outputMock, inputMock, testObj);
    floatFieldTest(control, outputMock, inputMock, testObj);
    doubleFieldTest(control, outputMock, inputMock, testObj);
    stringFieldTest(control, outputMock, inputMock, testObj);
    classFieldTest(control, outputMock, inputMock, testObj);
  }
  
  @SuppressWarnings("nls")
  public void test_type() throws Exception {
    FieldTypeTestClass obj = new FieldTypeTestClass();
    
    Class<? extends FieldTypeTestClass> class1 = obj.getClass();
    assertEquals(FieldType.booleanPrimitive, FieldType.getType(obj.getBooleanFieldP()));
    assertEquals(FieldType.booleanPrimitive, FieldType.getType(obj.getBooleanFieldC()));
    
    assertEquals(FieldType.charPrimitive, FieldType.getType(obj.getCharFieldP()));
    assertEquals(FieldType.charPrimitive, FieldType.getType(obj.getCharFieldC()));
    
    assertEquals(FieldType.bytePrimitive, FieldType.getType(obj.getByteFieldP()));
    assertEquals(FieldType.bytePrimitive, FieldType.getType(obj.getByteFieldC()));
    
    assertEquals(FieldType.shortPrimitive, FieldType.getType(obj.getShortFieldP()));
    assertEquals(FieldType.shortPrimitive, FieldType.getType(obj.getShortFieldC()));
    
    assertEquals(FieldType.integerPrimitive, FieldType.getType(obj.getIntFieldP()));
    assertEquals(FieldType.integerPrimitive, FieldType.getType(obj.getIntFieldC()));
    
    assertEquals(FieldType.longPrimitive, FieldType.getType(obj.getLongFieldP()));
    assertEquals(FieldType.longPrimitive, FieldType.getType(obj.getLongFieldC()));
    
    assertEquals(FieldType.floatPrimitive, FieldType.getType(obj.getFloatFieldP()));
    assertEquals(FieldType.floatPrimitive, FieldType.getType(obj.getFloatFieldC()));
    
    assertEquals(FieldType.doublePrimitive, FieldType.getType(obj.getDoubleFieldP()));
    assertEquals(FieldType.doublePrimitive, FieldType.getType(obj.getDoubleFieldC()));
    
    assertEquals(FieldType.string, FieldType.getType(class1.getDeclaredField("fString")));
    
    assertEquals(FieldType.clazz, FieldType.getType(class1.getDeclaredField("fObject")));
  }
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    fReaderContext = EasyMock.createNiceMock(IReaderContext.class);
    fSerializeContext = EasyMock.createNiceMock(ISerializeContext.class);
  }

  private void booleanFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock, FieldTypeTestClass testObj) throws IOException, Exception {
    testObj.fBooleanP = true;
    testObj.fBooleanC = new Boolean(true);
    
    outputMock.writeBoolean(true);
    outputMock.writeBoolean(true);
    
    EasyMock.expect(inputMock.readBoolean()).andReturn(true);
    EasyMock.expect(inputMock.readBoolean()).andReturn(true);
    
    control.replay();
    
    FieldType.booleanPrimitive.writeValue(outputMock, testObj, testObj.getBooleanFieldP(), fSerializeContext);
    FieldType.booleanPrimitive.writeValue(outputMock, testObj, testObj.getBooleanFieldC(), fSerializeContext);
    
    FieldType.booleanPrimitive.readValue(inputMock, testObj, testObj.getBooleanFieldP(), fReaderContext);
    FieldType.booleanPrimitive.readValue(inputMock, testObj, testObj.getBooleanFieldC(), fReaderContext);
    
    control.verify();
    
    assertTrue("FieldValue was not set correctly", testObj.fBooleanC);
    assertTrue("FieldValue was not set correctly", testObj.fBooleanP);
    
    control.reset();
  }

  private void byteFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock, FieldTypeTestClass testObj) throws Exception {
    testObj.fByteP = 5;
    testObj.fByteC = new Byte((byte)5);
    
    outputMock.writeByte(5);
    outputMock.writeByte(5);
    
    EasyMock.expect(inputMock.readByte()).andReturn((byte)6);
    EasyMock.expect(inputMock.readByte()).andReturn((byte)6);
    
    control.replay();
    
    FieldType.bytePrimitive.writeValue(outputMock, testObj, testObj.getByteFieldP(), fSerializeContext);
    FieldType.bytePrimitive.writeValue(outputMock, testObj, testObj.getByteFieldC(), fSerializeContext);
    
    FieldType.bytePrimitive.readValue(inputMock, testObj, testObj.getByteFieldP(), fReaderContext);
    FieldType.bytePrimitive.readValue(inputMock, testObj, testObj.getByteFieldC(), fReaderContext);
    
    control.verify();
    
    assertEquals("FieldValue was not set correctly", 6, testObj.fByteP);
    assertEquals("FieldValue was not set correctly", new Byte((byte)6), testObj.fByteC);
    
    control.reset();       
  }

  private void charFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock, FieldTypeTestClass testObj) throws Exception {
    testObj.fCharP = 'a';
    testObj.fCharC = new Character('a');
    
    outputMock.writeChar('a');
    outputMock.writeChar('a');
    
    EasyMock.expect(inputMock.readChar()).andReturn('b');
    EasyMock.expect(inputMock.readChar()).andReturn('b');
    
    control.replay();
    
    FieldType.charPrimitive.writeValue(outputMock, testObj, testObj.getCharFieldP(), fSerializeContext);
    FieldType.charPrimitive.writeValue(outputMock, testObj, testObj.getCharFieldC(), fSerializeContext);
    
    FieldType.charPrimitive.readValue(inputMock, testObj, testObj.getCharFieldP(), fReaderContext);
    FieldType.charPrimitive.readValue(inputMock, testObj, testObj.getCharFieldC(), fReaderContext);
    
    control.verify();
    
    assertEquals("FieldValue was not set correctly", 'b', testObj.fCharP);
    assertEquals("FieldValue was not set correctly", new Character('b'), testObj.fCharC);
    
    control.reset();    
  }

  private void classFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock, FieldTypeTestClass testObj) {}

  private void doubleFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock, FieldTypeTestClass testObj) throws Exception {
    testObj.fDoubleP = 5;
    testObj.fDoubleC = new Double(5);
    
    outputMock.writeDouble(5d);
    outputMock.writeDouble(5d);
    
    EasyMock.expect(inputMock.readDouble()).andReturn(new Double(6));
    EasyMock.expect(inputMock.readDouble()).andReturn(new Double(6));
    
    control.replay();
    
    FieldType.doublePrimitive.writeValue(outputMock, testObj, testObj.getDoubleFieldP(), fSerializeContext);
    FieldType.doublePrimitive.writeValue(outputMock, testObj, testObj.getDoubleFieldC(), fSerializeContext);
    
    FieldType.doublePrimitive.readValue(inputMock, testObj, testObj.getDoubleFieldP(), fReaderContext);
    FieldType.doublePrimitive.readValue(inputMock, testObj, testObj.getDoubleFieldC(), fReaderContext);
    
    control.verify();
    
    assertEquals("FieldValue was not set correctly", 6.0, testObj.fDoubleP);
    assertEquals("FieldValue was not set correctly", new Double(6.0), testObj.fDoubleC);
    
    control.reset();
  }

  private void floatFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock, FieldTypeTestClass testObj) throws Exception {
    testObj.fFloatP = 5;
    testObj.fFloatC = new Float(5);
    
    outputMock.writeFloat(5f);
    outputMock.writeFloat(5);
    
    EasyMock.expect(inputMock.readFloat()).andReturn(new Float(6));
    EasyMock.expect(inputMock.readFloat()).andReturn(new Float(6));
    
    control.replay();
    
    FieldType.floatPrimitive.writeValue(outputMock, testObj, testObj.getFloatFieldP(), fSerializeContext);
    FieldType.floatPrimitive.writeValue(outputMock, testObj, testObj.getFloatFieldC(), fSerializeContext);
    
    FieldType.floatPrimitive.readValue(inputMock, testObj, testObj.getFloatFieldP(), fReaderContext);
    FieldType.floatPrimitive.readValue(inputMock, testObj, testObj.getFloatFieldC(), fReaderContext);
    
    control.verify();
    
    assertEquals("FieldValue was not set correctly", 6, testObj.fIntP);
    assertEquals("FieldValue was not set correctly", new Integer(6), testObj.fIntC);
    
    control.reset();        
  }

  private void intFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock, FieldTypeTestClass testObj) throws Exception {
    testObj.fIntP = 5;
    testObj.fIntC = new Integer(5);
    
    outputMock.writeInt(5);
    outputMock.writeInt(5);
    
    EasyMock.expect(inputMock.readInt()).andReturn(6);
    EasyMock.expect(inputMock.readInt()).andReturn(6);
    
    control.replay();
    
    FieldType.integerPrimitive.writeValue(outputMock, testObj, testObj.getIntFieldP(), fSerializeContext);
    FieldType.integerPrimitive.writeValue(outputMock, testObj, testObj.getIntFieldC(), fSerializeContext);
    
    FieldType.integerPrimitive.readValue(inputMock, testObj, testObj.getIntFieldP(), fReaderContext);
    FieldType.integerPrimitive.readValue(inputMock, testObj, testObj.getIntFieldC(), fReaderContext);
    
    control.verify();
    
    assertEquals("FieldValue was not set correctly", 6, testObj.fIntP);
    assertEquals("FieldValue was not set correctly", new Integer(6), testObj.fIntC);
    
    control.reset();    
  }

  private void longFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock, FieldTypeTestClass testObj) throws Exception {
    testObj.fLongP = 5;
    testObj.fLongC = new Long(5);
    
    outputMock.writeLong(5);
    outputMock.writeLong(5);
    
    EasyMock.expect(inputMock.readLong()).andReturn(new Long(6));
    EasyMock.expect(inputMock.readLong()).andReturn(new Long(6));
    
    control.replay();
    
    FieldType.longPrimitive.writeValue(outputMock, testObj, testObj.getLongFieldP(), fSerializeContext);
    FieldType.longPrimitive.writeValue(outputMock, testObj, testObj.getLongFieldC(), fSerializeContext);
    
    FieldType.longPrimitive.readValue(inputMock, testObj, testObj.getLongFieldP(), fReaderContext);
    FieldType.longPrimitive.readValue(inputMock, testObj, testObj.getLongFieldC(), fReaderContext);
    
    control.verify();
    
    assertEquals("FieldValue was not set correctly", 6, testObj.fLongP);
    assertEquals("FieldValue was not set correctly", new Long(6), testObj.fLongC);
    
    control.reset();      
  }

  private void shortFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock, FieldTypeTestClass testObj) throws Exception {
    testObj.fShortP = 5;
    testObj.fShortC = new Short((short)5);
    
    outputMock.writeShort(5);
    outputMock.writeShort(5);
    
    EasyMock.expect(inputMock.readShort()).andReturn((short)6);
    EasyMock.expect(inputMock.readShort()).andReturn((short)6);
    
    control.replay();
    
    FieldType.shortPrimitive.writeValue(outputMock, testObj, testObj.getShortFieldP(), fSerializeContext);
    FieldType.shortPrimitive.writeValue(outputMock, testObj, testObj.getShortFieldC(), fSerializeContext);
    
    FieldType.shortPrimitive.readValue(inputMock, testObj, testObj.getShortFieldP(), fReaderContext);
    FieldType.shortPrimitive.readValue(inputMock, testObj, testObj.getShortFieldC(), fReaderContext);
    
    control.verify();
    
    assertEquals("FieldValue was not set correctly", 6, testObj.fShortP);
    assertEquals("FieldValue was not set correctly", new Short((short)6), testObj.fShortC);
    
    control.reset();        
  }

  private void stringFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock, FieldTypeTestClass testObj) throws Exception {
    testObj.fString = "1";
    
    outputMock.writeUTF("1");
    
    EasyMock.expect(inputMock.readUTF()).andReturn("2");
    
    control.replay();
    
    FieldType.string.writeValue(outputMock, testObj, testObj.getStringField(), fSerializeContext);
    
    FieldType.string.readValue(inputMock, testObj, testObj.getStringField(), fReaderContext);
    
    control.verify();
    
    assertEquals("FieldValue was not set correctly", "2", testObj.fString);
    control.reset();
  }
  

}
