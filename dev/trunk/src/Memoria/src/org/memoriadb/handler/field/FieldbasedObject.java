package org.memoriadb.handler.field;

import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.id.IObjectId;


public class FieldbasedObject implements IFieldbasedObject {

  private final Object fObejct;
  private IObjectId fMemoriaClassId;

  public FieldbasedObject(Object object) {
    fObejct = object;
  }
  
  public FieldbasedObject(Object object, IObjectId memoriaClassId) {
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
