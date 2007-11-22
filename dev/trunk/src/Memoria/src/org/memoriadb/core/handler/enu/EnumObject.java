package org.memoriadb.core.handler.enu;

import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.IMemoriaClass;
import org.memoriadb.util.ReflectionUtil;

public class EnumObject implements IEnumObject {
  
  private IObjectId fMemoriaClassId;
  private int fOrdinal;

  public EnumObject(Enum<?> enumObj) {
    fOrdinal = enumObj.ordinal();
  }

  public EnumObject(IObjectId memoriaClassId) {
    fMemoriaClassId = memoriaClassId;
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fMemoriaClassId;
  }

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
