package org.memoriadb.core.refactor;

import java.util.List;

import org.memoriadb.*;
import org.memoriadb.core.handler.IDataObject;
import org.memoriadb.core.handler.array.*;
import org.memoriadb.core.handler.enu.*;
import org.memoriadb.core.handler.field.*;
import org.memoriadb.core.id.IObjectId;
import org.memoriadb.core.meta.Type;
import org.memoriadb.exception.MemoriaException;
import org.memoriadb.util.*;

public class RefactorApi implements IRefactor {

  private final IDataStore fDataStore;

  public RefactorApi(IDataStore dataStore) {
    fDataStore = dataStore;
  }

  @Override
  public IFieldbasedObject asFieldDataObject(Object object) {
    if (object instanceof IFieldbasedObject) return (IFieldbasedObject) object;
    if (object instanceof IDataObject) throw new MemoriaException("no a field object " + object);
    IObjectId memoriaClassId = fDataStore.getTypeInfo().getMemoriaClassId(object.getClass());
    return new FieldbasedObject(object, memoriaClassId);
  }

  public IArray createArray(Class<?> klass, int length) {
    if (!klass.isArray()) throw new MemoriaException("not an array " + klass);
    return new DataArray(getArrayClass(), ReflectionUtil.getComponentTypeInfo(klass), length);
  }

  @Override
  public IArray createArray(String componentType, int dimension, int length) {
    ArrayTypeInfo info = new ArrayTypeInfo(Type.typeClass, dimension, componentType);
    return new DataArray(getArrayClass(), info, length);
  }

  @Override
  public IEnumObject getEnum(String name, final int ordinal) {
    List<IEnumObject> query = fDataStore.query(name, new IFilter<IEnumObject>() {

      @Override
      public boolean accept(IEnumObject object, IFilterControl control) {
        if (object.getOrdinal() == ordinal) {
          control.abort();
          return true;
        }
        return false;
      }

    });

    if (!query.isEmpty()) return query.get(0);

    IObjectId memoriaClassId = fDataStore.getTypeInfo().getMemoriaClassId(name);
    if (memoriaClassId == null) throw new MemoriaException("can not create enum, because enum-class has not yet been saved: " + name);
    return new EnumDataObject(memoriaClassId, ordinal);
  }

  private IObjectId getArrayClass() {
    return fDataStore.getDefaultIdProvider().getArrayMemoriaClass();
  }

}
