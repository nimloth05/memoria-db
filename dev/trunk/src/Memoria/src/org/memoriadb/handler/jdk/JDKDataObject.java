package org.memoriadb.handler.jdk;

import org.memoriadb.handler.IDataObject;
import org.memoriadb.id.IObjectId;

public class JDKDataObject<T> implements IDataObject {
  
  private T fObject;
  private final IObjectId fClassId;
  
  public static <E> JDKDataObject<E> create(IObjectId classId, E object) {
    return new JDKDataObject<E>(classId, object);
  }
  
  private JDKDataObject(IObjectId classId, T object) {
    fClassId = classId;
    fObject = object;
  }

  @Override
  public IObjectId getMemoriaClassId() {
    return fClassId;
  }
  
  public T getObject() {
    return fObject;
  }
  
  public void setObject(T object) {
    fObject = object;
  }
  
  @Override
  public String toString() {
    return fObject.toString();
  }

}
