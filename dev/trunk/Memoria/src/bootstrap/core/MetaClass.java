package bootstrap.core;

import java.io.*;
import java.lang.reflect.*;
import java.lang.reflect.Modifier;
import java.util.*;

public final class MetaClass {

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
      
      MetaField metaField = MetaField.create(++fieldId, field);
      addMetaField(metaField);
    }    
  }
  
  public long getTypeId() {
    return fId;
  }

  public void writeObject(DataOutput dataStream, Object object, long objectId) throws Exception {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream(80);
    DataOutputStream objectStream = new DataOutputStream(buffer);

    objectStream.writeLong(getTypeId());
    objectStream.writeLong(objectId);
    
    for(MetaField metaField: fFieldIdToInfo.values()) {
      metaField.writeField(objectStream, object, fClassName);
    }

    byte[] objectData = buffer.toByteArray();
    
    dataStream.writeInt(objectData.length);
    dataStream.write(objectData);    
  }
  
  /**
   * Only used for bootstrapping outside this class
   */
  private void addMetaField(MetaField metaField) {
    fFieldIdToInfo.put(metaField.getId(), metaField);
    fFieldNameToInfo.put(metaField.getName(), metaField);
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

}
