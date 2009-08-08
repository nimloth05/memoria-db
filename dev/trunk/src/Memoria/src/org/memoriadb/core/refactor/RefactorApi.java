package org.memoriadb.core.refactor;

import org.memoriadb.IDataStore;
import org.memoriadb.IFilter;
import org.memoriadb.IFilterControl;
import org.memoriadb.IRefactor;
import org.memoriadb.core.exception.MemoriaException;
import org.memoriadb.core.meta.Type;
import org.memoriadb.core.util.ArrayTypeInfo;
import org.memoriadb.core.util.ReflectionUtil;
import org.memoriadb.handler.IDataObject;
import org.memoriadb.handler.array.DataArray;
import org.memoriadb.handler.array.IArray;
import org.memoriadb.handler.enu.EnumDataObject;
import org.memoriadb.handler.enu.IEnumObject;
import org.memoriadb.handler.field.FieldbasedObject;
import org.memoriadb.handler.field.IFieldbasedObject;
import org.memoriadb.handler.value.LangValueObject;
import org.memoriadb.id.IObjectId;

import java.util.List;

public class RefactorApi implements IRefactor {

  private final IDataStore fDataStore;

  public RefactorApi(IDataStore dataStore) {
    fDataStore = dataStore;
  }

  @Override
  public IFieldbasedObject asFieldDataObject(Object object) {
    if (object instanceof IFieldbasedObject) return (IFieldbasedObject) object;
    if (object instanceof IDataObject) throw new MemoriaException("object is a DataObject but not of type IFieldbasedObject. " + object);
    
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
  public IEnumObject getEnum(String className, final String name) {
    List<IEnumObject> query = fDataStore.query(className, new IFilter<IEnumObject>() {

      @Override
      public boolean accept(IEnumObject object, IFilterControl control) {
        if (object.getName().equals(name)) {
          control.abort();
          return true;
        }
        return false;
      }

    });

    if (!query.isEmpty()) return query.get(0);

    IObjectId memoriaClassId = fDataStore.getTypeInfo().getMemoriaClassId(className);
    if (memoriaClassId == null) throw new MemoriaException("can not create enum, because enum-class has not yet been saved: " + className);
    return new EnumDataObject(memoriaClassId, name);
  }

  @Override
  public <T> LangValueObject<T> getLangValueObject(T value) {
    IObjectId memoriaClassId = fDataStore.getTypeInfo().getMemoriaClassId(value.getClass());
    return new LangValueObject<T>(value, memoriaClassId);
  }

  private IObjectId getArrayClass() {
    return fDataStore.getDefaultIdProvider().getArrayMemoriaClass();
  }

}
