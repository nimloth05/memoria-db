package org.memoriadb.test.core;

import java.io.*;

import junit.framework.TestCase;

import org.easymock.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.load.*;
import org.memoriadb.core.meta.Type;
import org.memoriadb.test.core.testclasses.*;

public class TypeTest extends TestCase {
  
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
    assertEquals(Type.typeBoolean, Type.getType(obj.getBooleanFieldP()));
    assertEquals(Type.typeBoolean, Type.getType(obj.getBooleanFieldC()));
    
    assertEquals(Type.typeChar, Type.getType(obj.getCharFieldP()));
    assertEquals(Type.typeChar, Type.getType(obj.getCharFieldC()));
    
    assertEquals(Type.typeByte, Type.getType(obj.getByteFieldP()));
    assertEquals(Type.typeByte, Type.getType(obj.getByteFieldC()));
    
    assertEquals(Type.typeShort, Type.getType(obj.getShortFieldP()));
    assertEquals(Type.typeShort, Type.getType(obj.getShortFieldC()));
    
    assertEquals(Type.typeInteger, Type.getType(obj.getIntFieldP()));
    assertEquals(Type.typeInteger, Type.getType(obj.getIntFieldC()));
    
    assertEquals(Type.typeLong, Type.getType(obj.getLongFieldP()));
    assertEquals(Type.typeLong, Type.getType(obj.getLongFieldC()));
    
    assertEquals(Type.typeFloat, Type.getType(obj.getFloatFieldP()));
    assertEquals(Type.typeFloat, Type.getType(obj.getFloatFieldC()));
    
    assertEquals(Type.typeDouble, Type.getType(obj.getDoubleFieldP()));
    assertEquals(Type.typeDouble, Type.getType(obj.getDoubleFieldC()));
    
    assertEquals(Type.typeString, Type.getType(class1.getDeclaredField("fString")));
    
    assertEquals(Type.typeClass, Type.getType(class1.getDeclaredField("fObject")));
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
    
    Type.typeBoolean.writeFieldValue(outputMock, testObj, testObj.getBooleanFieldP(), fSerializeContext);
    Type.typeBoolean.writeFieldValue(outputMock, testObj, testObj.getBooleanFieldC(), fSerializeContext);
    
    Type.typeBoolean.readFieldValue(inputMock, testObj, testObj.getBooleanFieldP(), fReaderContext);
    Type.typeBoolean.readFieldValue(inputMock, testObj, testObj.getBooleanFieldC(), fReaderContext);
    
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
    
    Type.typeByte.writeFieldValue(outputMock, testObj, testObj.getByteFieldP(), fSerializeContext);
    Type.typeByte.writeFieldValue(outputMock, testObj, testObj.getByteFieldC(), fSerializeContext);
    
    Type.typeByte.readFieldValue(inputMock, testObj, testObj.getByteFieldP(), fReaderContext);
    Type.typeByte.readFieldValue(inputMock, testObj, testObj.getByteFieldC(), fReaderContext);
    
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
    
    Type.typeChar.writeFieldValue(outputMock, testObj, testObj.getCharFieldP(), fSerializeContext);
    Type.typeChar.writeFieldValue(outputMock, testObj, testObj.getCharFieldC(), fSerializeContext);
    
    Type.typeChar.readFieldValue(inputMock, testObj, testObj.getCharFieldP(), fReaderContext);
    Type.typeChar.readFieldValue(inputMock, testObj, testObj.getCharFieldC(), fReaderContext);
    
    control.verify();
    
    assertEquals("FieldValue was not set correctly", 'b', testObj.fCharP);
    assertEquals("FieldValue was not set correctly", new Character('b'), testObj.fCharC);
    
    control.reset();    
  }

  private void classFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock, FieldTypeTestClass testObj) throws Exception {
    testObj.fObject = new SimpleTestObj("1");
    
    EasyMock.expect(fSerializeContext.getObjectId(testObj.fObject)).andStubReturn(new Long(1));
    outputMock.writeLong(1);
    
    EasyMock.expect(inputMock.readLong()).andReturn(new Long(1));
    fReaderContext.objectToBind((IBindable) EasyMock.anyObject());
    
    control.replay();
    EasyMock.replay(fSerializeContext, fReaderContext);
    
    Type.typeClass.writeFieldValue(outputMock, testObj, testObj.getObjectField(), fSerializeContext);
    
    Type.typeClass.readFieldValue(inputMock, testObj, testObj.getObjectField(), fReaderContext);
    
    control.verify();
    EasyMock.verify(fSerializeContext, fReaderContext);
  }

  private void doubleFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock, FieldTypeTestClass testObj) throws Exception {
    testObj.fDoubleP = 5;
    testObj.fDoubleC = new Double(5);
    
    outputMock.writeDouble(5d);
    outputMock.writeDouble(5d);
    
    EasyMock.expect(inputMock.readDouble()).andReturn(new Double(6));
    EasyMock.expect(inputMock.readDouble()).andReturn(new Double(6));
    
    control.replay();
    
    Type.typeDouble.writeFieldValue(outputMock, testObj, testObj.getDoubleFieldP(), fSerializeContext);
    Type.typeDouble.writeFieldValue(outputMock, testObj, testObj.getDoubleFieldC(), fSerializeContext);
    
    Type.typeDouble.readFieldValue(inputMock, testObj, testObj.getDoubleFieldP(), fReaderContext);
    Type.typeDouble.readFieldValue(inputMock, testObj, testObj.getDoubleFieldC(), fReaderContext);
    
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
    
    Type.typeFloat.writeFieldValue(outputMock, testObj, testObj.getFloatFieldP(), fSerializeContext);
    Type.typeFloat.writeFieldValue(outputMock, testObj, testObj.getFloatFieldC(), fSerializeContext);
    
    Type.typeFloat.readFieldValue(inputMock, testObj, testObj.getFloatFieldP(), fReaderContext);
    Type.typeFloat.readFieldValue(inputMock, testObj, testObj.getFloatFieldC(), fReaderContext);
    
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
    
    Type.typeInteger.writeFieldValue(outputMock, testObj, testObj.getIntFieldP(), fSerializeContext);
    Type.typeInteger.writeFieldValue(outputMock, testObj, testObj.getIntFieldC(), fSerializeContext);
    
    Type.typeInteger.readFieldValue(inputMock, testObj, testObj.getIntFieldP(), fReaderContext);
    Type.typeInteger.readFieldValue(inputMock, testObj, testObj.getIntFieldC(), fReaderContext);
    
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
    
    Type.typeLong.writeFieldValue(outputMock, testObj, testObj.getLongFieldP(), fSerializeContext);
    Type.typeLong.writeFieldValue(outputMock, testObj, testObj.getLongFieldC(), fSerializeContext);
    
    Type.typeLong.readFieldValue(inputMock, testObj, testObj.getLongFieldP(), fReaderContext);
    Type.typeLong.readFieldValue(inputMock, testObj, testObj.getLongFieldC(), fReaderContext);
    
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
    
    Type.typeShort.writeFieldValue(outputMock, testObj, testObj.getShortFieldP(), fSerializeContext);
    Type.typeShort.writeFieldValue(outputMock, testObj, testObj.getShortFieldC(), fSerializeContext);
    
    Type.typeShort.readFieldValue(inputMock, testObj, testObj.getShortFieldP(), fReaderContext);
    Type.typeShort.readFieldValue(inputMock, testObj, testObj.getShortFieldC(), fReaderContext);
    
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
    
    Type.typeString.writeFieldValue(outputMock, testObj, testObj.getStringField(), fSerializeContext);
    
    Type.typeString.readFieldValue(inputMock, testObj, testObj.getStringField(), fReaderContext);
    
    control.verify();
    
    assertEquals("FieldValue was not set correctly", "2", testObj.fString);
    control.reset();
  }
  

}
