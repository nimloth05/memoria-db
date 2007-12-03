package org.memoriadb.handler.enu;

import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.id.IObjectId;

public class EnumObject implements IEnumObject {
  
  private IObjectId fMemoriaClassId;
  private int fOrdinal;
  
  public EnumObject(Enum<?> enumObj) {
    fOrdinal = enumObj.ordinal();
  }

  public EnumObject(IObjectId memoriaClassId) {
    fMemoriaClassId = memoriaClassId;
  }
  
  public EnumObject(IObjectId memoriaClassId, Enum<?> enumObj) {
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
    Class<Enum<?>> enumClass = (Class<Enum<?>>) ReflectionUtil.getClass(memoriaClass.getJavaClassName());
    return enumClass.getEnumConstants()[fOrdinal];
  }

  @Override
  public int getOrdinal() {
    return fOrdinal;
  }

  @Override
  public void setOrdinal(int ordinal) {
    fOrdinal = ordinal;
  }

}
