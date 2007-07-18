package bootstrap.core;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import bootstrap.exception.MemoriaException;

import com.sun.org.apache.bcel.internal.generic.FNEG;

public final class MetaClass {

  private static final long METACLASS_OBJECT_ID = 1;
  
  private final long fId;
  private String fClassName;

  transient private Map<Integer, MetaField> fFieldIdToInfo = new HashMap<Integer, MetaField>();
  // this mapping must no be saved, its just a "helper"
  transient private Map<String, MetaField> fFieldNameToInfo = new HashMap<String, MetaField>();

  /**
   * Introspects the given klass and adds all fields. Used to initially create a MetaClass, when the first
   * object of a given type enters the memoria-reference-space.
   *
   */
  public MetaClass(long typeId, Class<?> klass) {
    this(typeId);
    fClassName = klass.getName();
    
    addFields(klass);
  }

  /**
   * This constructor should only be used for reconstitution. 
   */
  private MetaClass(long typeId) {
    fId = typeId;
  }
  
  private void addFields(Class<?> klass) {
    Field[] fields = klass.getDeclaredFields();
    int fieldId = 1;
    for(Field field: fields) {
      if (Modifier.isTransient(field.getModifiers())) continue;
      if (Modifier.isFinal(field.getModifiers())) continue;
      
      MetaField metaField = MetaField.create(++fieldId, field);
      addMetaField(metaField);
    }    
  }
  
  /**
   * Only used for bootstrapping outside this class
   */
  private void addMetaField(MetaField metaField) {
    fFieldIdToInfo.put(metaField.getId(), metaField);
    fFieldNameToInfo.put(metaField.getName(), metaField);
  }
  
  public long getTypeId() {
    return fId;
  }

  public void writeObject(DataOutput dataStream, Object object, long objectId) throws Exception {
    if(!object.getClass().getName().equals(fClassName)) throw new MemoriaException("object of type " + fClassName + " expected but was " + object.getClass());
    
    System.out.println("metaClass id: " + fId);
    
    ByteArrayOutputStream buffer = new ByteArrayOutputStream(80);
    DataOutputStream objectStream = new DataOutputStream(buffer);

    objectStream.writeLong(getTypeId());
    objectStream.writeLong(objectId);
    
    
    if(isMetaClassObject()) {
      writeMetaClass(objectStream, object);
    }
    else {
      writeObject(objectStream, object);
    }
    
    byte[] objectData = buffer.toByteArray();
    dataStream.writeInt(objectData.length);
    dataStream.write(objectData);    
  }

  private void writeObject(DataOutputStream objectStream, Object object) throws Exception  {
    for(MetaField metaField: fFieldIdToInfo.values()) {
      metaField.writeField(objectStream, object);
    }
  }

  private void writeMetaClass(DataOutputStream objectStream, Object object) {
    
  }

  public static MetaClass create(long typeId) {
    return new MetaClass(typeId);
  }

  public void setClassName(String name) {
    fClassName = name;
  }

  public String getClassName() {
    return fClassName;
  }

  @Override
  public String toString() {
    return fClassName;
  }
  
  /**
   * @return true, if this MetaClass-object represents the type MetaClass
   */
  public boolean isMetaClassObject() {
    return fId == METACLASS_OBJECT_ID;
  }

}
