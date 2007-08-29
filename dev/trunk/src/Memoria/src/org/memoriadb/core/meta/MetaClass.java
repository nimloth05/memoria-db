package org.memoriadb.core.meta;

import java.lang.reflect.*;
import java.util.*;

import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.exception.MemoriaException;


public final class MetaClass implements IMetaClassConfig {

  private String fClassName;

  private final Map<Integer, MetaField> fFieldIdToInfo = new HashMap<Integer, MetaField>();
  private final Map<String, MetaField> fFieldNameToInfo = new HashMap<String, MetaField>();

  private IMetaClass fSuperClass;

  /**
   * @return true, if this MetaClass-object represents the type MetaClass
   */
  public static boolean isMetaClassObject(long typeId) {
    return typeId == METACLASS_OBJECT_ID;
  }
  
  /**
   * Introspects the given klass and adds all fields. Used to initially create a MetaClass, when the first
   * object of a given type enters the memoria-reference-space.
   *
   */
  public MetaClass(Class<?> klass) {
    fClassName = klass.getName();
    
    addFields(klass);
  }
  
  public MetaClass(String className) {
    fClassName = className;
  }
  
  public void addMetaField(MetaField metaField) {
    fFieldIdToInfo.put(metaField.getId(), metaField);
    fFieldNameToInfo.put(metaField.getName(), metaField);
  }

  public String getClassName() {
    return fClassName;
  }

  public MetaField getField(int fieldId) {
    return fFieldIdToInfo.get(fieldId);
  }

  public int getFieldCount() {
    return fFieldIdToInfo.values().size();
  }

  public Iterable<MetaField> getFields() {
    return fFieldIdToInfo.values();
  }
  
  @Override
  public ISerializeHandler getHandler() {
    return new org.memoriadb.core.handler.def.DefaultHandler(this);
  }
  
  /* (non-Javadoc)
   * @see org.memoriadb.core.IMetaClass#getJavaClass()
   */
  public Class<?> getJavaClass() { 
    try {
      return Class.forName(fClassName);
    } catch (Exception e) {
      throw new MemoriaException(e);
    }
  }
  
  @Override
  public IMetaClass getSuperClass() {
    return fSuperClass;
  }

  /* (non-Javadoc)
   * @see org.memoriadb.core.IMetaClass#newInstance()
   */
  public Object newInstance()  {
    try {
      return getJavaClass().newInstance();
    }
    catch (Exception e) {
      throw new MemoriaException("Unable to instantiate " + fClassName + " (default-ctor missing?)");
    }
  }

  public void setClassName(String name) {
    fClassName = name;
  }

  public void setSuperClass(IMetaClass superClass) {
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
      
      MetaField metaField = MetaField.create(++fieldId, field);
      addMetaField(metaField);
    }    
  }
  
}
