package org.memoriadb.test.core.testclasses;

import java.lang.reflect.Field;

@SuppressWarnings("nls")
public class FieldTypeTestClass {
  
  public boolean fBooleanP;
  public Boolean fBooleanC;
  
  public char fCharP;
  public Character fCharC;
  
  public byte fByteP;
  public Byte fByteC;
  
  public short fShortP;
  public Short fShortC;
  
  public int fIntP;
  public Integer fIntC;
  
  public long fLongP;
  public Long fLongC;
  
  public float fFloatP;
  public Float fFloatC;
  
  public double fDoubleP;
  public Double fDoubleC;
  
  public String fString;
  
  public Object fObject;

  
  public Field getBooleanFieldC() throws Exception {
    return getField("fBooleanC");
  }

  public Field getBooleanFieldP() throws Exception {
    return getField("fBooleanP");
  }
  
  public Field getByteFieldC() throws Exception {
    return getField("fByteC");
  }
  
  public Field getByteFieldP() throws Exception {
    return getField("fByteP");
  }

  public Field getCharFieldC() throws Exception {
    return getField("fCharC");
  }

  public Field getCharFieldP() throws Exception {
    return getField("fCharP");
  }
  
  public Field getDoubleFieldC() throws Exception {
    return getField("fDoubleC");
  }

  public Field getDoubleFieldP() throws Exception {
    return getField("fDoubleP");
  }
  
  public Field getFloatFieldC() throws Exception {
    return getField("fFloatC");
  }

  public Field getFloatFieldP() throws Exception {
    return getField("fFloatP");
  }

  public Field getIntFieldC() throws Exception {
    return getField("fIntC");
  }

  public Field getIntFieldP() throws Exception {
    return getField("fIntP");
  }
  
  public Field getLongFieldC() throws Exception {
    return getField("fLongC");
  }

  public Field getLongFieldP() throws Exception {
    return getField("fLongP");
  }
  
  public Field getObjectField() throws Exception {
    return getField("fObject");
  }

  public Field getShortFieldC() throws Exception {
    return getField("fShortC");
  }

  public Field getShortFieldP() throws Exception {
    return getField("fShortP");
  }

  public Field getStringField() throws Exception {
    return getField("fString");
  }

  private Field getField(String name) throws Exception {
    Class<? extends FieldTypeTestClass> class1 = getClass();
    return class1.getDeclaredField(name);
  }
  

}
