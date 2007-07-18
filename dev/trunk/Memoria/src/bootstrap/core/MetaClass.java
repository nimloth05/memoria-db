package bootstrap.core;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import bootstrap.exception.MemoriaException;

public final class MetaClass {

  private static final long METACLASS_OBJECT_ID = 1;
  
  private String fClassName;

  private Map<Integer, MetaField> fFieldIdToInfo = new HashMap<Integer, MetaField>();
  private Map<String, MetaField> fFieldNameToInfo = new HashMap<String, MetaField>();

  /**
   * Introspects the given klass and adds all fields. Used to initially create a MetaClass, when the first
   * object of a given type enters the memoria-reference-space.
   *
   */
  public MetaClass(Class<?> klass) {
    fClassName = klass.getName();
    
    addFields(klass);
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
  
  public void writeObject(DataOutput dataStream, Object object, long objectId) throws Exception {
    if(!object.getClass().getName().equals(fClassName)) throw new MemoriaException("object of type " + fClassName + " expected but was " + object.getClass());
    
    long typeId = objectStore.getId(this);
    System.out.println("metaClass id: " + fId);
    
    ByteArrayOutputStream buffer = new ByteArrayOutputStream(80);
    DataOutputStream objectStream = new DataOutputStream(buffer);
    
    objectStream.writeLong(typeId);
    objectStream.writeLong(objectId);
    
    if(isMetaClassObject(typeId)) {
      writeMetaClass(objectStream, (MetaClass) object);
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

  private void writeMetaClass(DataOutput objectStream, MetaClass classObject) throws IOException {
    objectStream.writeUTF(classObject.getClassName());
    writeFields(objectStream, classObject);
  }

  private void writeFields(DataOutput objectStream, MetaClass classObject) throws IOException {
    for(MetaField field: fFieldIdToInfo.values()) {
      objectStream.writeInt(field.getId());
      objectStream.writeUTF(field.getName());
      objectStream.writeInt(field.getType());
    }
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
  public boolean isMetaClassObject(long typeId) {
    return typeId == METACLASS_OBJECT_ID;
  }

}
