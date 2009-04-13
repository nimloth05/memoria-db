package org.memoriadb.handler.value;

import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.IObjectId;

/**
 * ValueObject for the java language primitive Value-Objects.
 * 
 * @author Nienor
 *
 * @param <T> Type of the primitive
 */
public class LangValueObject<T> implements IDataObject {

  private T fObject;
  private final IObjectId fClassId;
  
  public LangValueObject(T object, IObjectId classId) {
    fObject = object;
    fClassId = classId;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    final LangValueObject<?> other = (LangValueObject<?>) obj;
    if (fClassId == null) {
      if (other.fClassId != null) return false;
    }
    else if (!fClassId.equals(other.fClassId)) return false;
    if (fObject == null) {
      if (other.fObject != null) return false;
    }
    else if (!fObject.equals(other.fObject)) return false;
    return true;
  }
  
  public T get() {
    return fObject;
  }
  
  @Override
  public IObjectId getMemoriaClassId() {
    return fClassId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fClassId == null) ? 0 : fClassId.hashCode());
    result = prime * result + ((fObject == null) ? 0 : fObject.hashCode());
    return result;
  }

  public void set(T t) {
    fObject = t;
  }
  
}
