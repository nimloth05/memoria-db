package org.memoriadb.handler.field;

import java.util.*;

import org.memoriadb.handler.value.LangValueObject;
import org.memoriadb.id.IObjectId;

public class FieldbasedDataObject implements IFieldbasedObject {
  
  private final Map<String, Object> fData = new HashMap<String, Object>();
  private final IObjectId fMemoriaClassId;
  
  public FieldbasedDataObject(IObjectId memoriaClassId) {
    fMemoriaClassId = memoriaClassId;
  }

  @Override
  public boolean equalsLangValueObject(String fieldName, Object value) {
    return ((LangValueObject<?>)get(fieldName)).get().equals(value);
  }

  public Object get(String string) {
    return fData.get(string);
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fMemoriaClassId;
  }

  @Override
  public Object getObject() {
    return this;
  }

  @Override
  public void set(String fieldName, Object value) {
    fData.put(fieldName, value);
  }

}
