package org.memoriadb.core.handler.field;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.util.ReflectionUtil;


public class FieldObject implements IFieldObject {

  private final Object fObejct;
  private IObjectId fMemoriaClassId;

  public FieldObject(Object object) {
    fObejct = object;
  }
  
  public FieldObject(Object object, IObjectId memoriaClassId) {
    fObejct = object;
    fMemoriaClassId = memoriaClassId;
  }
  
  @Override
  public Object get(String fieldName) {
    return ReflectionUtil.getValueFromField(fObejct, fieldName);
  }

  @Override
  public IObjectId getMemoriaClassId() {
    if(fMemoriaClassId == null) throw new UnsupportedOperationException();
    return fMemoriaClassId;
  }

  @Override
  public Object getObject() {
    return fObejct;
  }

  @Override
  public void set(String fieldName, Object value) {
    ReflectionUtil.setValueForField(fObejct, fieldName, value); 
  }

}
