package org.memoriadb.handler.enu;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.id.IObjectId;

public class EnumObject implements IEnumObject {
  
  private IObjectId fMemoriaClassId;
  private String fName;

  public EnumObject(Enum<? extends Enum<?>> enumObj) {
    fName = enumObj.name();
  }

  public EnumObject(IObjectId memoriaClassId) {
    fMemoriaClassId = memoriaClassId;
  }
  
  public EnumObject(IObjectId memoriaClassId, Enum<? extends Enum<?>> enumObj) {
    this(enumObj);
    fMemoriaClassId = memoriaClassId;
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fMemoriaClassId;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object getObject(IMemoriaClass memoriaClass) {
    Class enumClass = (Class) ReflectionUtil.getClass(memoriaClass.getJavaClassName());
    return Enum.valueOf(enumClass, fName);
  }

  @Override
  public String getName() {
    return fName;
  }

  @Override
  public void setName(String name) {
    fName = name;
  }

}
