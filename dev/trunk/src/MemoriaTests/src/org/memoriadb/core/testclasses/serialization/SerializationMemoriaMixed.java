package org.memoriadb.core.testclasses.serialization;

import java.io.Serializable;

import org.memoriadb.core.testclasses.SimpleTestObj;

public class SerializationMemoriaMixed implements Serializable {
  
  private Serialization fJavaObject;
  private SimpleTestObj fMemoriaObject;

}
