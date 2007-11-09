package org.memoriadb.core.meta;

import java.lang.reflect.*;
import java.util.*;

import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.core.id.IObjectId;


public final class MemoriaFieldClass implements IMemoriaClassConfig {

  private String fClassName;

  private final Map<Integer, MemoriaField> fFieldIdToInfo = new HashMap<Integer, MemoriaField>();
  private final Map<String, MemoriaField> fFieldNameToInfo = new HashMap<String, MemoriaField>();

  private IMemoriaClass fSuperClass;
  private final IObjectId fMemoriaClassId;

  /**
   * Introspects the given klass and adds all fields. Used to initially create a MetaClass, when the first
   * object of a given type enters the memoria-reference-space.
   *
   */
  public MemoriaFieldClass(Class<?> klass, IObjectId memoriaClassId) {
    fMemoriaClassId = memoriaClassId;
    fClassName = klass.getName();
    
    addFields(klass);
  }
  
  public MemoriaFieldClass(String className, IObjectId memoriaClassId) {
    fClassName = className;
    fMemoriaClassId = memoriaClassId;
  }
  
  public void addMetaField(MemoriaField metaField) {
    fFieldIdToInfo.put(metaField.getId(), metaField);
    fFieldNameToInfo.put(metaField.getName(), metaField);
  }

  public String getClassName() {
    return fClassName;
  }

  public MemoriaField getField(int fieldId) {
    return fFieldIdToInfo.get(fieldId);
  }

  public int getFieldCount() {
    return fFieldIdToInfo.values().size();
  }

  public Iterable<MemoriaField> getFields() {
    return fFieldIdToInfo.values();
  }
  
  @Override
  public ISerializeHandler getHandler() {
    return new org.memoriadb.core.handler.def.field.DefaultHandler(this);
  }
  
  @Override
  public String getJavaClassName() {
    return fClassName;
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fMemoriaClassId;
  }
  
  @Override
  public IMemoriaClass getSuperClass() {
    return fSuperClass;
  }

  @Override
  public boolean isTypeFor(String javaClass) {
    return fClassName.equals(javaClass);
  }

  public void setClassName(String name) {
    fClassName = name;
  }

  public void setSuperClass(IMemoriaClass superClass) {
    fSuperClass = superClass;
  }

  @Override
  public String toString() {
    return fClassName;
  }

  private void addFields(Class<?> klass) {
    Field[] fields = klass.getDeclaredFields();
    int fieldId = 0;
    for(Field field: fields) {
      if (Modifier.isTransient(field.getModifiers())) continue;
      
      MemoriaField metaField = MemoriaField.create(++fieldId, field);
      addMetaField(metaField);
    }    
  }
  
}
