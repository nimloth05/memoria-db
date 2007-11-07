package org.memoriadb.core.handler.def.field;

import java.util.*;

public class FieldMapDataObject implements IFieldObject {
  
  private final Map<String, Object> fData = new HashMap<String, Object>();

  public Object get(String string) {
    return fData.get(string);
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
