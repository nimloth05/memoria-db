/*
 * Copyright 2010 Sandro Orlando
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.memoriadb.test.testclasses;

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
