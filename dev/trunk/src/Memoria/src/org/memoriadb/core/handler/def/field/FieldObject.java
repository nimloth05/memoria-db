package org.memoriadb.core.handler.def.field;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.util.ReflectionUtil;


public class FieldObject implements IFieldObject {

  private final Object fObejct;

  public FieldObject(Object object) {
    fObejct = object;
  }
  
  @Override
  public Object get(String fieldName) {
    return ReflectionUtil.getValueFromField(fObejct, fieldName);
  }

  @Override
  public IObjectId getMemoriaClassId() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object getObject() {
    return fObejct;
  }

  @Override
  public void set(String fieldName, Object value) {
    ReflectionUtil.setValueFromField(fObejct, fieldName, value); 
  }

}
