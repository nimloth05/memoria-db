package org.memoriadb.test.core;

import java.io.*;

import junit.framework.TestCase;

import org.easymock.*;
import org.memoriadb.core.file.ISerializeContext;
import org.memoriadb.core.id.def.LongObjectId;
import org.memoriadb.core.load.IReaderContext;
import org.memoriadb.core.meta.*;
import org.memoriadb.test.core.testclasses.*;

public class TypeTest extends TestCase {
  
  private IReaderContext fReaderContext;
  private ISerializeContext fSerializeContext;

  public void test_read_write_field() throws Exception {
    IMocksControl control = EasyMock.createControl();
    
    DataOutput outputMock = control.createMock(DataOutput.class);
    DataInput inputMock = control.createMock(DataInput.class);
    
    booleanFieldTest(control, outputMock, inputMock);
    charFieldTest(control, outputMock, inputMock);
    byteFieldTest(control, outputMock, inputMock);
    shortFieldTest(control, outputMock, inputMock);
    intFieldTest(control, outputMock, inputMock);
    longFieldTest(control, outputMock, inputMock);
    floatFieldTest(control, outputMock, inputMock);
    doubleFieldTest(control, outputMock, inputMock);
    stringFieldTest(control, outputMock, inputMock);
    classFieldTest(control, outputMock, inputMock);
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

  private void booleanFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock) throws IOException, Exception {
    boolean valueP = true;
    Boolean valueC = new Boolean(valueP);
    
    boolean readValue = false;
    
    outputMock.writeBoolean(valueP);
    outputMock.writeBoolean(valueP);
    
    EasyMock.expect(inputMock.readBoolean()).andReturn(readValue);
    EasyMock.expect(inputMock.readBoolean()).andReturn(readValue);
    
    ITypeVisitor vistiorMock = createPrimtiveVistiroMock(control, Type.typeBoolean, readValue);
    
    control.replay();
    
    Type.typeBoolean.writeValue(outputMock, valueP, fSerializeContext);
    Type.typeBoolean.writeValue(outputMock, valueC, fSerializeContext);
    
    Type.typeBoolean.readValue(inputMock, fReaderContext, vistiorMock);
    Type.typeBoolean.readValue(inputMock, fReaderContext, vistiorMock);
    
    control.verify();
    
    control.reset();
  }

  private void byteFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock) throws Exception {
    byte valueP = 5;
    Byte valueC = new Byte(valueP);
    
    byte readValue = 6;
    
    outputMock.writeByte(valueP);
    outputMock.writeByte(valueP);
    
    EasyMock.expect(inputMock.readByte()).andReturn(readValue);
    EasyMock.expect(inputMock.readByte()).andReturn(readValue);
    
    ITypeVisitor vistiorMock = createPrimtiveVistiroMock(control, Type.typeByte, readValue);
    
    control.replay();
    
    Type.typeByte.writeValue(outputMock, valueP, fSerializeContext);
    Type.typeByte.writeValue(outputMock, valueC, fSerializeContext);
    
    Type.typeByte.readValue(inputMock, fReaderContext, vistiorMock);
    Type.typeByte.readValue(inputMock, fReaderContext, vistiorMock);
    
    control.verify();
    
    control.reset();       
  }

  private void charFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock) throws Exception {
    char valueP = 'a';
    Character valueC = new Character(valueP);
    
    char readValue = 'b';
    
    outputMock.writeChar(valueP);
    outputMock.writeChar(valueP);
    
    EasyMock.expect(inputMock.readChar()).andReturn(readValue);
    EasyMock.expect(inputMock.readChar()).andReturn(readValue);
    
    ITypeVisitor vistiorMock = createPrimtiveVistiroMock(control, Type.typeChar, readValue);
    
    control.replay();
    
    Type.typeChar.writeValue(outputMock, valueP, fSerializeContext);
    Type.typeChar.writeValue(outputMock, valueC, fSerializeContext);
    
    Type.typeChar.readValue(inputMock, fReaderContext, vistiorMock);
    Type.typeChar.readValue(inputMock, fReaderContext, vistiorMock);
    
    control.verify();
    
    control.reset();    
  }

  private void classFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock) throws Exception {
    Object obj = new SimpleTestObj("1");
    
    EasyMock.expect(fSerializeContext.getObjectId(obj)).andStubReturn(new LongObjectId(1));
    outputMock.writeLong(1);
    
    EasyMock.expect(fReaderContext.createFrom(inputMock)).andReturn(new LongObjectId(1));
    
    ITypeVisitor visitorMock = control.createMock(ITypeVisitor.class);
    visitorMock.visitClass(Type.typeClass, new LongObjectId(1));
    
    control.replay();
    EasyMock.replay(fSerializeContext, fReaderContext);
    
    Type.typeClass.writeValue(outputMock, obj, fSerializeContext);
    
    Type.typeClass.readValue(inputMock, fReaderContext, visitorMock);
    
    control.verify();
    EasyMock.verify(fSerializeContext, fReaderContext);
  }

  private ITypeVisitor createPrimtiveVistiroMock(IMocksControl control, Type type, Object value) {
    ITypeVisitor vistiorMock = control.createMock(ITypeVisitor.class);
    vistiorMock.visitPrimitive(type, value);
    vistiorMock.visitPrimitive(type, value);
    return vistiorMock;
  }

  private void doubleFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock) throws Exception {
    double valueP = 5;
    Double valueC = new Double(valueP);
    
    double readValue = 6;
    
    outputMock.writeDouble(valueP);
    outputMock.writeDouble(valueP);
    
    EasyMock.expect(inputMock.readDouble()).andReturn(readValue);
    EasyMock.expect(inputMock.readDouble()).andReturn(readValue);
    
    ITypeVisitor vistiorMock = createPrimtiveVistiroMock(control, Type.typeDouble, readValue);
    
    control.replay();
    
    Type.typeDouble.writeValue(outputMock, valueP, fSerializeContext);
    Type.typeDouble.writeValue(outputMock, valueC, fSerializeContext);
    
    Type.typeDouble.readValue(inputMock, fReaderContext, vistiorMock);
    Type.typeDouble.readValue(inputMock, fReaderContext, vistiorMock);
    
    control.verify();
    
    control.reset();
  }

  private void floatFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock) throws Exception {
    float valueP = 5f;
    Float valueC = new Float(valueP);
    
    float readValue = 6;
    
    outputMock.writeFloat(valueP);
    outputMock.writeFloat(valueP);
    
    EasyMock.expect(inputMock.readFloat()).andReturn(readValue);
    EasyMock.expect(inputMock.readFloat()).andReturn(readValue);
    
    ITypeVisitor vistiorMock = createPrimtiveVistiroMock(control, Type.typeFloat, readValue);
    
    control.replay();
    
    Type.typeFloat.writeValue(outputMock, valueP, fSerializeContext);
    Type.typeFloat.writeValue(outputMock, valueC, fSerializeContext);
    
    Type.typeFloat.readValue(inputMock, fReaderContext, vistiorMock);
    Type.typeFloat.readValue(inputMock, fReaderContext, vistiorMock);
    
    control.verify();
    
    control.reset();        
  }

  private void intFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock) throws Exception {
    int valueP = 5;
    Integer valueC = new Integer(valueP);
    
    int readValue = 6;
    
    outputMock.writeInt(valueP);
    outputMock.writeInt(valueP);
    
    EasyMock.expect(inputMock.readInt()).andReturn(readValue);
    EasyMock.expect(inputMock.readInt()).andReturn(readValue);
    
    ITypeVisitor vistiorMock = createPrimtiveVistiroMock(control, Type.typeInteger, readValue);
    
    control.replay();
    
    Type.typeInteger.writeValue(outputMock, valueP, fSerializeContext);
    Type.typeInteger.writeValue(outputMock, valueC, fSerializeContext);
    
    Type.typeInteger.readValue(inputMock, fReaderContext, vistiorMock);
    Type.typeInteger.readValue(inputMock, fReaderContext, vistiorMock);
    
    control.verify();
    
    control.reset();    
  }

  private void longFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock) throws Exception {
    long valueP = 5;
    Long valueC = new Long(valueP);
    
    long readValue = 6;
    
    outputMock.writeLong(valueP);
    outputMock.writeLong(valueP);
    
    EasyMock.expect(inputMock.readLong()).andReturn(readValue);
    EasyMock.expect(inputMock.readLong()).andReturn(readValue);
    
    ITypeVisitor vistiorMock = createPrimtiveVistiroMock(control, Type.typeLong, readValue);
    
    control.replay();
    
    Type.typeLong.writeValue(outputMock, valueP, fSerializeContext);
    Type.typeLong.writeValue(outputMock, valueC, fSerializeContext);
    
    Type.typeLong.readValue(inputMock, fReaderContext, vistiorMock);
    Type.typeLong.readValue(inputMock, fReaderContext, vistiorMock);
    
    control.verify();
    
    control.reset();      
  }

  private void shortFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock) throws Exception {
    short valueP = 5;
    Short valueC = new Short((short)5);
    
    Short readValueC = new Short((short)6);
    
    outputMock.writeShort(5);
    outputMock.writeShort(5);
    
    EasyMock.expect(inputMock.readShort()).andReturn(readValueC);
    EasyMock.expect(inputMock.readShort()).andReturn(readValueC);
    
    ITypeVisitor vistiorMock = createPrimtiveVistiroMock(control, Type.typeShort, readValueC);
    
    control.replay();
    
    Type.typeShort.writeValue(outputMock, valueP, fSerializeContext);
    Type.typeShort.writeValue(outputMock, valueC, fSerializeContext);
    
    Type.typeShort.readValue(inputMock, fReaderContext, vistiorMock);
    Type.typeShort.readValue(inputMock, fReaderContext, vistiorMock);
    
    control.verify();
    
    control.reset();        
  }

  private void stringFieldTest(IMocksControl control, DataOutput outputMock, DataInput inputMock) throws Exception {
    String value = "1";
    String readValue = "2";
    
    outputMock.writeUTF(value);
    
    EasyMock.expect(inputMock.readUTF()).andReturn(readValue);
    
    ITypeVisitor vistiorMock = control.createMock(ITypeVisitor.class);
    vistiorMock.visitPrimitive(Type.typeString, readValue);
    
    control.replay();
    
    Type.typeString.writeValue(outputMock, value, fSerializeContext);
    
    Type.typeString.readValue(inputMock, fReaderContext, vistiorMock);
    
    control.verify();
    
    control.reset();
  }
  
}
