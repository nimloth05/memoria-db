package org.memoriadb.core.meta;

import java.lang.reflect.Field;
import java.util.*;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.handler.IHandler;

public final class FieldbasedMemoriaClass extends AbstractMemoriaClass {

  private String fClassName;

  private final Map<Integer, MemoriaField> fFieldIdToInfo = new HashMap<Integer, MemoriaField>();
  private final Map<String, MemoriaField> fFieldNameToInfo = new HashMap<String, MemoriaField>();

  private IMemoriaClass fSuperClass;
  private final IObjectId fMemoriaClassId;

  /**
   * Introspects the given klass and adds all fields. Used to initially create a MetaClass, when the first object of a
   * given type enters the memoria-reference-space.
   * 
   */
  public FieldbasedMemoriaClass(Class<?> klass, IObjectId memoriaClassId) {
    fMemoriaClassId = memoriaClassId;
    fClassName = klass.getName();

    addFields(klass);
  }

  public FieldbasedMemoriaClass(String className, IObjectId memoriaClassId) {
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
  public IHandler getHandler() {
    return new org.memoriadb.handler.field.FieldbasedObjectHandler(this);
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
    for (Field field : fields) {
      if (ReflectionUtil.isStatic(field)) continue;
      if (ReflectionUtil.isMemoriaTransient(field)) continue;

      MemoriaField metaField = MemoriaField.create(++fieldId, field);
      addMetaField(metaField);
    }
  }

}
