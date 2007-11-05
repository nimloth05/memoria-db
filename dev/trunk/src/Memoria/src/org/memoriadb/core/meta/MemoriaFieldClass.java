package org.memoriadb.core.meta;

import java.lang.reflect.*;
import java.util.*;

import org.memoriadb.core.handler.ISerializeHandler;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.IdConstants;


public final class MemoriaFieldClass implements IMemoriaClassConfig {

  private String fClassName;

  private final Map<Integer, MemoriaField> fFieldIdToInfo = new HashMap<Integer, MemoriaField>();
  private final Map<String, MemoriaField> fFieldNameToInfo = new HashMap<String, MemoriaField>();

  private IMemoriaClass fSuperClass;

  /**
   * @return true, if this MetaClass-object represents the type MetaClass
   */
  public static boolean isMetaClassObject(long typeId) {
    return typeId == IdConstants.METACLASS_OBJECT_ID;
  }
  
  /**
   * Introspects the given klass and adds all fields. Used to initially create a MetaClass, when the first
   * object of a given type enters the memoria-reference-space.
   *
   */
  public MemoriaFieldClass(Class<?> klass) {
    fClassName = klass.getName();
    
    addFields(klass);
  }
  
  public MemoriaFieldClass(String className) {
    fClassName = className;
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
  public IMemoriaClass getSuperClass() {
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
      throw new MemoriaException("Unable to instantiate " + fClassName + " (default-ctor missing?) " + e.getMessage() );
    }
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
